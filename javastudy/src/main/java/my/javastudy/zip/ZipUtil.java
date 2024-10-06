package my.javastudy.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
	public static void main( String args[] ) throws Exception {
		ZipUtil zipUtil = new ZipUtil();
		zipUtil.compressFile("/Volumes/mydisk/test/test.db", "/Volumes/mydisk/test/test.zip");
		zipUtil.unCompressFile("/Volumes/mydisk/test/test.zip", "/Volumes/mydisk/test/test4uncompress");
		
		zipUtil.compressDir("/Volumes/mydisk/test/AwesomeProject", "/Volumes/mydisk/test/maximo.zip");
		zipUtil.unCompressFile("/Volumes/mydisk/test/maximo.zip", "/Volumes/mydisk/test/test4uncompress");
	}
	
	public void compressFile (String file, String zipFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		File fileToZip = new File(file);
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
		zipOut.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length = 0;
		while((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		fis.close();
		zipOut.closeEntry();
		zipOut.finish();
		zipOut.close();
		fos.close();
	}
	
	public void compressDir(String dir, String zipFile) throws IOException {
		FileOutputStream fos = new FileOutputStream(zipFile);
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		
		File fileToZip = new File(dir);
		zipFile(fileToZip, fileToZip.getName(), zipOut);
		zipOut.finish();
		zipOut.close();
		fos.close();
	}
	
	public void unCompressFile(String zipFile, String destDir) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File destFile = new File(destDir, zipEntry.getName());
			if (zipEntry.isDirectory()) {
				if (!destFile.isDirectory() && !destFile.mkdirs()) {
					throw new IOException("Failed to create directory " + destFile);
				}
			} else {
				File parent = destFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					throw new IOException("Failed to create directory " + parent);
				}
				
				FileOutputStream fos = new FileOutputStream(destFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		
		zis.closeEntry();
		zis.close();
	}
	
	private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}
}
