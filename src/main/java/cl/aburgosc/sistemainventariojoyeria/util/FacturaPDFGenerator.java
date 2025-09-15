package cl.aburgosc.sistemainventariojoyeria.util;

import java.awt.Desktop;
import java.io.File;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import cl.aburgosc.sistemainventariojoyeria.model.Cliente;
import cl.aburgosc.sistemainventariojoyeria.model.DetalleVenta;
import cl.aburgosc.sistemainventariojoyeria.model.Producto;
import cl.aburgosc.sistemainventariojoyeria.model.Venta;
import cl.aburgosc.sistemainventariojoyeria.service.impl.ProductoServiceImpl;

public class FacturaPDFGenerator {

	public static void generarFactura(Venta venta, Cliente cliente, String rutaArchivo) throws Exception {
		Locale cl = new Locale.Builder().setLanguage("es").setRegion("CL").build();
		NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(cl);
		moneyFormat.setMaximumFractionDigits(0);

		PdfWriter writer = new PdfWriter(rutaArchivo);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);

		PdfFont fontNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

		Paragraph titulo = new Paragraph("JOYERÍA ABURGOSC").setFont(fontBold).setFontSize(18)
				.setTextAlignment(TextAlignment.CENTER);
		Paragraph factura = new Paragraph("FACTURA").setFont(fontBold).setFontSize(16)
				.setTextAlignment(TextAlignment.CENTER);
		document.add(titulo);
		document.add(factura);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Table infoTable = new Table(UnitValue.createPercentArray(new float[] { 1, 2 })).useAllAvailableWidth()
				.setMarginTop(10).setMarginBottom(20);

		infoTable.addCell(new Cell().add(new Paragraph("Cliente:").setFont(fontBold)).setBorder(null));
		infoTable.addCell(
				new Cell().add(new Paragraph(cliente.getNombre() + " " + cliente.getApellido()).setFont(fontNormal))
						.setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph("RUT:").setFont(fontBold)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph(cliente.getRut()).setFont(fontNormal)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph("Dirección:").setFont(fontBold)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph(cliente.getDireccion()).setFont(fontNormal)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph("Teléfono:").setFont(fontBold)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph(cliente.getTelefono()).setFont(fontNormal)).setBorder(null));
		infoTable.addCell(new Cell().add(new Paragraph("Fecha:").setFont(fontBold)).setBorder(null));
		infoTable.addCell(
				new Cell().add(new Paragraph(sdf.format(venta.getFecha())).setFont(fontNormal)).setBorder(null));

		document.add(infoTable);

		Table tablaProductos = new Table(UnitValue.createPercentArray(new float[] { 3, 1, 1, 1 }))
				.useAllAvailableWidth();
		tablaProductos.addHeaderCell(
				new Cell().add(new Paragraph("Producto").setFont(fontBold).setFontColor(ColorConstants.WHITE))
						.setBackgroundColor(ColorConstants.DARK_GRAY).setTextAlignment(TextAlignment.CENTER));
		tablaProductos.addHeaderCell(
				new Cell().add(new Paragraph("Cantidad").setFont(fontBold).setFontColor(ColorConstants.WHITE))
						.setBackgroundColor(ColorConstants.DARK_GRAY).setTextAlignment(TextAlignment.CENTER));
		tablaProductos.addHeaderCell(
				new Cell().add(new Paragraph("Precio Unitario").setFont(fontBold).setFontColor(ColorConstants.WHITE))
						.setBackgroundColor(ColorConstants.DARK_GRAY).setTextAlignment(TextAlignment.CENTER));
		tablaProductos.addHeaderCell(
				new Cell().add(new Paragraph("Subtotal").setFont(fontBold).setFontColor(ColorConstants.WHITE))
						.setBackgroundColor(ColorConstants.DARK_GRAY).setTextAlignment(TextAlignment.CENTER));

		List<DetalleVenta> detalles = venta.getDetalleVentas();
		if (detalles != null) {
			for (DetalleVenta det : detalles) {
				Producto p = new ProductoServiceImpl().obtenerPorId(det.getIdProducto());

				tablaProductos
						.addCell(new Cell().add(new Paragraph(String.valueOf(p.getNombre())).setFont(fontNormal)));
				tablaProductos
						.addCell(new Cell().add(new Paragraph(String.valueOf(det.getCantidad())).setFont(fontNormal)));
				tablaProductos.addCell(new Cell().add(
						new Paragraph(moneyFormat.format(det.getPrecioUnitario().setScale(2, RoundingMode.HALF_UP)))
								.setFont(fontNormal)));
				tablaProductos.addCell(new Cell()
						.add(new Paragraph(moneyFormat.format(det.getSubtotal().setScale(2, RoundingMode.HALF_UP)))
								.setFont(fontNormal)));
			}
		}

		document.add(tablaProductos);

		Paragraph total = new Paragraph(
				"TOTAL: " + moneyFormat.format(venta.calcularTotal().setScale(2, RoundingMode.HALF_UP)))
				.setFont(fontBold).setFontSize(14).setTextAlignment(TextAlignment.RIGHT).setMarginTop(15)
				.setMarginBottom(15);
		document.add(total);

		document.add(new Paragraph("-----------------------------------------------------------")
				.setTextAlignment(TextAlignment.CENTER));

		// ----- Footer -----
		Paragraph footer = new Paragraph("Gracias por su compra. ¡Vuelva pronto!").setFont(fontNormal).setFontSize(10)
				.setTextAlignment(TextAlignment.CENTER).setMarginTop(10);
		document.add(footer);

		document.close();

		File pdfFile = new File(rutaArchivo);
		if (Desktop.isDesktopSupported() && pdfFile.exists()) {
			Desktop.getDesktop().open(pdfFile);
		}
	}
}
