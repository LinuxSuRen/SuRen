/**
 * 
 */
package org.suren.core.net;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import com.jcraft.jsch.UserInfo;

/**
 * @author SuRen
 */
public class UserInfoSimple implements UserInfo
{

	private JComponent	parent;

	public UserInfoSimple() {
	}

	public UserInfoSimple(JComponent parent) {
		this.parent = parent;
	}

	public String getPassphrase()
	{
		return null;
	}

	public String getPassword()
	{
		return JOptionPane.showInputDialog(parent, "Please Input your word", "Type info",
				JOptionPane.OK_CANCEL_OPTION);
	}

	public boolean promptPassphrase(String arg0)
	{
		return false;
	}

	public boolean promptPassword(String arg0)
	{
		return true;
	}

	public boolean promptYesNo(String arg0)
	{
		return true;
	}

	public void showMessage(String arg0)
	{
	}

}
