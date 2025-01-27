package fr.paris.lutece.plugins.qrcodegenerator.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutce.plugins.qrcode.QRcodeGenerator;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfig;
import fr.paris.lutece.plugins.qrcodegenerator.business.QrCodeConfigHome;
import fr.paris.lutece.portal.service.file.FileServiceException;
import fr.paris.lutece.portal.service.file.IFileStoreServiceProvider;
import fr.paris.lutece.portal.service.util.AppException;
import fr.paris.lutece.portal.service.util.AppLogService;


public class QrCodeDisplay extends HttpServlet
{	
	private static final long serialVersionUID = 9005457149901889275L;

	private static final String MARK_QRCODE_CONFIG = "qrconfig";
	private static final String MARK_MESSAGE = "msg";
	
	private static final String IMG_BASE64_OPENER = "<img src='data:image/png;base64,";
    private static final String IMG_BASE64_CLOSER = "' />";
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        int nIdQrCodeConfig = request.getParameter( MARK_QRCODE_CONFIG ) == null ? -1 : Integer.parseInt(request.getParameter( MARK_QRCODE_CONFIG ));
		
        QrCodeConfig qrCodeConfig = null;
		try {
			qrCodeConfig = QrCodeConfigHome.findByPrimaryKey( nIdQrCodeConfig ).orElse( QrCodeConfig.getDefaultQrCodeConfig( ) );
		} catch ( NumberFormatException e ) {
			AppLogService.error( "Error parsing the id of the QrCodeConfig", e );
			throw new AppException( "Error parsing the id of the QrCodeConfig", e );
		}
		
		String _strMessage;
		if (qrCodeConfig.getConfigurationType() == QrCodeConfig.ConfigurationType.URL.getValue()) {
			_strMessage = request.getParameter(MARK_MESSAGE);
		} else {
			StringBuilder sb = new StringBuilder();
			for (String param : request.getParameterMap().keySet()) {
				if (param.equals(MARK_QRCODE_CONFIG)) {
					continue;
				}
				sb.append(param);
				sb.append("=");
				sb.append(request.getParameter(param));
				sb.append("&");
			}
			_strMessage = sb.toString();
		}

		QRcodeGenerator qRcodeGenerator = new QRcodeGenerator(_strMessage);
		BufferedImage img = null;
		try {
			img = qRcodeGenerator.createBufferedImage();
		} catch (UnsupportedEncodingException e) {
			AppLogService.error("Error creating QR code");
			throw new AppException("Error creating QR code");
		}
		if (qrCodeConfig.getImageLogo() != null) {
			IFileStoreServiceProvider fileStoreService = QrCodeConfigHome.getFileStoreServiceProvider( );
			try {
				QRcodeGenerator.addLogoToQRCode(img, fileStoreService.getInputStream( qrCodeConfig.getImageLogo( ).getFileKey( ) ), 0.2);
			} catch (FileServiceException e) {
				AppLogService.error("Error reading logo file", e);
				throw new AppException("Error reading logo file", e);
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", baos);
		} catch (IOException e) {
			AppLogService.error("Error writing QR code to byte array");
			throw new AppException("Error writing QR code to byte array");
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(IMG_BASE64_OPENER);
		sb.append(Base64.getEncoder().encodeToString(baos.toByteArray()));
		sb.append(IMG_BASE64_CLOSER);
		
		response.setContentType("text/html");
		try {
			response.getWriter().write(sb.toString());
		} catch (IOException e) {
			AppLogService.error("Error writing QR code to response");
			throw new AppException("Error writing QR code to response");
		}
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
    {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * 
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     * @throws ServletException
     *             the servlet Exception
     * @throws IOException
     *             the io exception
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response )
    {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     * 
     * @return message
     */
    @Override
    public String getServletInfo( )
    {
        return "Servlet serving Qr Code images";
    }
}
