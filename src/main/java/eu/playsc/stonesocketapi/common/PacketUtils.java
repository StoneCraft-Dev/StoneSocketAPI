package eu.playsc.stonesocketapi.common;

import eu.playsc.stonesocketapi.Logger;

import java.io.ByteArrayOutputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class PacketUtils {

	public static int findSizeOfObject(byte[] data) {
		int i = data.length - 1;
		while (i >= 0 && data[i] == 0) {
			--i;
		}

		return i + 1;
	}

	public static byte[] getObjectFromPacket(byte[] data) {
		int index;
		try {
			index = findSizeOfObject(data);

			byte[] objectArray = new byte[index - 10];

			System.arraycopy(data, 10, objectArray, 0, objectArray.length);

			return objectArray;
		} catch (Exception e) {
			Logger.error(e);
		}

		return null;
	}

	public static String getChecksumOfObject(byte[] data) {
		Checksum checksum = new CRC32();
		checksum.update(data, 0, data.length);
		StringBuilder val = new StringBuilder(String.valueOf(checksum.getValue()));
		while (val.length() < 10) {
			val.append("0");
		}

		return val.toString();
	}

	public static byte[] getByteArray(ByteArrayOutputStream stream) {
		byte[] array = stream.toByteArray();

		if (array[array.length - 1] == 0) {
			byte[] temp = new byte[array.length + 1];
			System.arraycopy(array, 0, temp, 0, array.length);
			temp[temp.length - 1] = (byte) -995;
			array = temp;
		}

		byte[] checksumBytes = getChecksumOfObject(array).getBytes();

		byte[] concat = new byte[2048];

		System.arraycopy(checksumBytes, 0, concat, 0, checksumBytes.length);
		System.arraycopy(array, 0, concat, checksumBytes.length, array.length);

		return concat;
	}
}
