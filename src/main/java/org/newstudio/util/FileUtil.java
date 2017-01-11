package org.newstudio.util;

import javax.annotation.Nonnull;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;

public final class FileUtil {
	/**
	 * Util 類別不允許直接初始化。
	 */
	private FileUtil() {
		throw new UnsupportedOperationException("Util class cannot be initiated directly.");
	}

	/**
	 * 把 byte 數字格式化成 KB 字串，保證小數點兩位。
	 *
	 * @param value 檔案大小 (byte)
	 * @return KB 字串 (保證小數點兩位)
	 */
	public static String sizeByteToKilobyte(long value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive.");
		}

		double Size = (double) value / 1024;
		NumberFormat formatter = DecimalFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		formatter.setGroupingUsed(false);
		return formatter.format(Size);
	}

	/**
	 * 複製檔案。
	 *
	 * @param srcPath 檔案位置
	 * @param destPath 目的位置
	 * @return 是否成功
	 */
	public static boolean copy(@Nonnull String srcPath, @Nonnull String destPath) {
		Objects.requireNonNull(srcPath, "The source is null.");
		Objects.requireNonNull(destPath, "The destination is null.");

		return copy(new File(srcPath), new File(destPath));
	}

	/**
	 * 複製檔案。
	 *
	 * @param srcFile 檔案位置
	 * @param destFile 目的位置
	 * @return 是否成功
	 */
	public static boolean copy(@Nonnull File srcFile, @Nonnull File destFile) {
		Objects.requireNonNull(srcFile, "The source is null.");
		Objects.requireNonNull(destFile, "The destination is null.");

		boolean result = true;
		try {
			InputStream in = new FileInputStream(srcFile);
			OutputStream out = new FileOutputStream(destFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();

			// 將檔案最後修改時間設成與來源檔相同
			destFile.setLastModified(srcFile.lastModified());
		} catch (Exception ex) {
			result = false;
		}
		return result;
	}
}
