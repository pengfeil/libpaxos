package org.pengfeil.lib.libpaxos.io.model;

/**
 * Represent a communication address of a agent.
 * 
 */
public class Endpoint {
	private String IpAddress;
	private long port;

	public Endpoint(String ipAddress, long port) {
		super();
		IpAddress = ipAddress;
		this.port = port;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public long getPort() {
		return port;
	}

	public void setPort(long port) {
		this.port = port;
	}

	public boolean isValid() {
		return IpAddress != null && port > 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((IpAddress == null) ? 0 : IpAddress.hashCode());
		result = prime * result + (int) (port ^ (port >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endpoint other = (Endpoint) obj;
		if (IpAddress == null) {
			if (other.IpAddress != null)
				return false;
		} else if (!IpAddress.equals(other.IpAddress))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Endpoint [IpAddress=" + IpAddress + ", port=" + port + "]";
	}

}
