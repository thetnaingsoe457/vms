package com.tns.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeGenerationService {
	private static final Logger logger = LoggerFactory.getLogger(QRCodeGenerationService.class);

	public byte[] generate(String promoCode) {
		byte[] result = null;
		int size = 256;
		try {

			Map<EncodeHintType, Object> hintType = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintType.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			hintType.put(EncodeHintType.MARGIN, 1); /* default = 4 */

			QRCodeWriter qrCodeWriter = new QRCodeWriter(); // throws com.google.zxing.WriterException
			BitMatrix bitMatrix = qrCodeWriter.encode(promoCode, BarcodeFormat.QR_CODE, size, size, hintType);
			int width = bitMatrix.getWidth();

			// The BufferedImage subclass describes an Image with an accessible buffer of
			// image data.
			BufferedImage bufferedImage = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);

			// Creates a Graphics2D, which can be used to draw into this BufferedImage.
			bufferedImage.createGraphics();

			// This Graphics2D class extends the Graphics class to provide more
			// sophisticated control over geometry, coordinate transformations, color
			// management, and text layout.
			// This is the fundamental class for rendering 2-dimensional shapes, text and
			// images on the Java(tm) platform.
			Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

			// setColor() sets this graphics context's current color to the specified color.
			// All subsequent graphics operations using this graphics context use this
			// specified color.
			graphics.setColor(Color.white);

			// fillRect() fills the specified rectangle. The left and right edges of the
			// rectangle are at x and x + width - 1.
			graphics.fillRect(0, 0, width, width);

			// QR Image Color
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < width; i++) {
				for (int j = 0; j < width; j++) {
					if (bitMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}

			// Save to local file
//			String filePath = "E:\\QROutput/" + promoCode + ".png";
//			String fileType = "png";
//			File file = new File(filePath);
//			ImageIO.write(bufferedImage, fileType, file);

			// Convert to byte[
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			result = baos.toByteArray();
			logger.info(promoCode + " : Generated QR Image.");
		} catch (WriterException e) {
			logger.error("Sorry.. Something went wrong..." + promoCode);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
