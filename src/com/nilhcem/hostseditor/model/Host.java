package com.nilhcem.hostseditor.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.common.net.InetAddresses;

public class Host implements Parcelable {
	public static final String STR_COMMENT = "#";
	private static final String STR_SEPARATOR = " ";
	private static final String HOST_PATTERN_STR = "^\\s*(" + STR_COMMENT + "?)\\s*(\\S*)\\s*(.*)$";
	private static final Pattern HOST_PATTERN = Pattern.compile(HOST_PATTERN_STR);

	private String mIp;
	private String mHostName;
	private boolean mIsCommented;
	private boolean mIsValid;

	public Host(String ip, String hostName, boolean isCommented, boolean isValid) {
		mIp = ip;
		mHostName = hostName;
		mIsCommented = isCommented;
		mIsValid = isValid;
	}

	public void merge(Host src) {
		mIp = src.getIp();
		mHostName = src.getHostName();
		mIsCommented = src.isCommented();
		mIsValid = src.isValid();
	}

	public String getIp() {
		return mIp;
	}

	public String getHostName() {
		return mHostName;
	}

	public boolean isValid() {
		return mIsValid;
	}

	public boolean isCommented() {
		return mIsCommented;
	}

	public void toggleComment() {
		mIsCommented = !mIsCommented;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (mIsCommented) {
			sb.append(STR_COMMENT);
		}
		if (mIp != null) {
			sb.append(mIp).append(STR_SEPARATOR);
		}
		if (mHostName != null) {
			sb.append(mHostName);
		}
		return sb.toString();
	}

	public static Host fromString(String line) {
		Matcher matcher = HOST_PATTERN.matcher(line);
		String ip = null;
		String hostname = null;
		boolean isCommented = false;
		boolean isValid = false;

		if (matcher.find()) {
			isCommented = !TextUtils.isEmpty(matcher.group(1));
			ip = matcher.group(2);
			hostname = matcher.group(3);

			isValid = !TextUtils.isEmpty(ip) && !TextUtils.isEmpty(hostname)
					&& InetAddresses.isInetAddress(ip);
		}
		return new Host(ip, hostname, isCommented, isValid);
	}

	public static final Parcelable.Creator<Host> CREATOR = new Parcelable.Creator<Host>() {

		@Override
		public Host createFromParcel(Parcel source) {
			return new Host(source);
		}

		@Override
		public Host[] newArray(int size) {
			return new Host[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mIp);
		dest.writeString(mHostName);
		dest.writeByte((byte) (mIsCommented ? 1 : 0));
		dest.writeByte((byte) (mIsValid ? 1 : 0));
	}

	private Host(Parcel in) {
		mIp = in.readString();
		mHostName = in.readString();
		mIsCommented = in.readByte() == 1;
		mIsValid = in.readByte() == 1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mHostName == null) ? 0 : mHostName.hashCode());
		result = prime * result + ((mIp == null) ? 0 : mIp.hashCode());
		result = prime * result + (mIsCommented ? 1231 : 1237);
		result = prime * result + (mIsValid ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Host other = (Host) obj;
		if (mHostName == null) {
			if (other.mHostName != null) {
				return false;
			}
		} else if (!mHostName.equals(other.mHostName)) {
			return false;
		}
		if (mIp == null) {
			if (other.mIp != null) {
				return false;
			}
		} else if (!mIp.equals(other.mIp)) {
			return false;
		}
		if (mIsCommented != other.mIsCommented) {
			return false;
		}
		if (mIsValid != other.mIsValid) {
			return false;
		}
		return true;
	}
}
