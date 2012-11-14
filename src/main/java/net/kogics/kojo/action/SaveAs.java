/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.kogics.kojo.core.KojoCtx;
import net.kogics.kojo.lite.CodeExecutionSupport;

public final class SaveAs implements ActionListener {
	private KojoCtx ctx;

	public SaveAs(KojoCtx ctx) {
		this.ctx = ctx;
	}

	public File chooseFile(String desc, String ext, String title) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(desc, ext);
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle(title);

		String loadDir = ctx.getLastLoadStoreDir();
		if (loadDir != null && loadDir != "") {
			File dir = new File(loadDir);
			if (dir.exists() && dir.isDirectory()) {
				chooser.setCurrentDirectory(dir);
			}
		}

		int returnVal = chooser.showSaveDialog(ctx.frame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ctx.setLastLoadStoreDir(chooser.getSelectedFile().getParent());
			File selectedFile = chooser.getSelectedFile();
			if (!selectedFile.getName().contains(".")) {
				selectedFile = new File(selectedFile.getAbsolutePath() + "."
						+ ext);
			}
			return selectedFile;
		} else {
			return null;
		}
	}

	public void actionPerformed(ActionEvent e) {

		File selectedFile = chooseFile("Kojo Files", "kojo",
				net.kogics.kojo.util.Utils.stripDots(e.getActionCommand()));

		if (selectedFile != null) {
			try {
				CodeExecutionSupport ces = (CodeExecutionSupport) CodeExecutionSupport
						.instance();
				ces.saveAs(selectedFile);
				ces.openFileWithoutClose(selectedFile);
			} catch (IllegalArgumentException ex) {
				// user said no to over-writing selected file
				// try again
				actionPerformed(e);
			} catch (RuntimeException ex) {
				// user cancelled save
			} catch (Throwable t) {
                System.out.println(String.format("Unable to save file: %s", t.getMessage()));
            }			
		}
	}
}
