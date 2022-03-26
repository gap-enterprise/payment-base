package io.surati.gap.payment.db;

import io.surati.gap.commons.utils.convert.CentimeterToPixel;
import io.surati.gap.database.utils.exceptions.DatabaseException;
import io.surati.gap.payment.api.Image;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.imaging.ImageReadException;
import org.apache.poi.util.IOUtils;
import org.cactoos.text.Joined;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public final class DbImage implements Image {
	
	private final DataSource source;

	private final Long meanid;
	
	static {
		File file = new File("images");
		if(!file.exists()) {
			file.mkdir();
		}
	}
	
	public DbImage(final DataSource source, final Long meanid) {
		this.source = source;
		this.meanid = meanid;
	}
	
	@Override
	public double width() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT width FROM payment_mean",
        				"WHERE id=?"
        			).toString()
				)
		){
			pstmt.setLong(1, this.meanid);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public double height() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT height FROM payment_mean",
        				"WHERE id=?"
        			).toString()
				)
		){
			pstmt.setLong(1, this.meanid);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getDouble(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	@Override
	public InputStream content() throws IOException {
		final Optional<String> fileopt = this.imageFilename();
		final InputStream in;
		if(fileopt.isPresent()) {
			in = new FileInputStream(
				new File(String.format("images/%s", fileopt.get()))
			);
		} else {
			final Double widthcm = this.width();
			final Double heightcm = this.height();
			final int widthdoc = new CentimeterToPixel(widthcm).value().intValue();
			final int heigthdoc = new CentimeterToPixel(heightcm).value().intValue();
			final BufferedImage buffer = new BufferedImage(widthdoc, heigthdoc, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = buffer.createGraphics();
			g2d.setComposite(AlphaComposite.Src);
		    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.black);
			g2d.setBackground(Color.white);
			g2d.clearRect(0, 0, widthdoc, heigthdoc);
			g2d.dispose();
			final ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageIO.write(buffer, "jpg", bs);
			byte[] bytes = bs.toByteArray();
			in = new ByteArrayInputStream(bytes);
		}
		return in;
	}

	@Override
	public void update(final InputStream content, final String ext) throws IOException {
		final String rootfilename = String.format("bank_note_image_payment_mean_%s_", meanid);
		Files.walk(Paths.get("images"))
			.filter(
				path -> path.getFileName().toString().startsWith(rootfilename)
			).forEach(
				path -> {
					try {
						Files.delete(path);
					} catch (IOException ex) {
						throw new RuntimeException(ex);
					}
				}
			);
		final String filenametosave = String.format("images/%s%s.%s", rootfilename, UUID.randomUUID(), ext);
		final Path path = new File(filenametosave).toPath();
        Files.copy(content, path);
        try {
			final Image fimage = new FileImage(new FileInputStream(path.toFile()));
			this.update(fimage.width(), fimage.height(), fimage.dpi());
		} catch (ImageReadException | IOException ex) {
			throw new RuntimeException(ex);
		}
        
	}
	
	@Override
	public String contentBase64() {
		try {
			final InputStream in = this.content();
			byte[] bytes = IOUtils.toByteArray(in);
			return String.format("data:image/jpg;base64,%s", Base64.encodeBase64String(bytes));
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void update(double width, double height, int dpi) {
		try (
			final Connection connection = source.getConnection();				
			final PreparedStatement pstmt = connection.prepareStatement("UPDATE payment_mean SET width=?, height=?, dpi=? WHERE id=?")
		) {
			pstmt.setDouble(1, width);
			pstmt.setDouble(2, height);
			pstmt.setInt(3, dpi);
			pstmt.setLong(4, this.meanid);
			pstmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}

	private final Optional<String> imageFilename() throws IOException {
		try (Stream<Path> files = Files.walk(Paths.get("images"))) {
	        return files.filter(f -> f.getFileName().toString().startsWith(String.format("bank_note_image_payment_mean_%s_", this.meanid)))
                .map(f -> f.getFileName().toString())
                .findFirst();
	    }
	}

	@Override
	public double widthpx() {
		return new CentimeterToPixel(this.dpi(), this.width()).value();
	}

	@Override
	public double heightpx() {
		return new CentimeterToPixel(this.dpi(), this.height()).value();
	}

	@Override
	public int dpi() {
		try (
			final Connection connection = source.getConnection();
			final PreparedStatement pstmt =
				connection.prepareStatement(
					new Joined(
        				" ",
        				"SELECT dpi FROM payment_mean",
        				"WHERE id=?"
        			).toString()
				)
		){
			pstmt.setLong(1, this.meanid);
			try (final ResultSet rs = pstmt.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
		} catch (SQLException ex) {
			throw new DatabaseException(ex);
		}
	}
}
