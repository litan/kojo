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

import net.kogics.kojo.lite.EditorFileSupport;
import net.kogics.kojo.util.FileChooser;

public final class SaveAs implements ActionListener {
    private EditorFileSupport fileSupport;
    FileChooser fileChooser;

    public SaveAs(EditorFileSupport fileSupport) {
        this.fileSupport = fileSupport;
        this.fileChooser = new FileChooser(fileSupport.kojoCtx());
    }

    public void actionPerformed(ActionEvent e) {
        File selectedFile = fileChooser.chooseFile("Kojo Files", "kojo",
                net.kogics.kojo.util.Utils.stripDots(e.getActionCommand()));

        if (selectedFile != null) {
            try {
                fileSupport.saveAs(selectedFile);
                fileSupport.openFileWithoutClose(selectedFile);
            }
            catch (IllegalArgumentException ex) {
                // user said no to over-writing selected file
                // try again
                actionPerformed(e);
            }
            catch (RuntimeException ex) {
                // user cancelled save
            }
            catch (Throwable t) {
                System.out.println(String.format("Unable to save file: %s",
                        t.getMessage()));
            }
        }
    }
}
