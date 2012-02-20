package org.suren.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class ClipboardUtil
{

	public static void send2Clipboard(String content)
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		clipboard.setContents(new StringSelection(content), null);
	}

}
