/*
 * Copyright (c) 2015 Patrick Scheibe
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package de.halirutan.mathematica.runconfig;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ListCellRendererWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author patrick (29.01.15)
 */
public class MathematicaRunPackageEditor extends SettingsEditor<MathematicaPackageRunConfiguration> {

  private JComboBox myNotebookSelector;
  private JTabbedPane configTab;
  private JTextField myLoadPackage;
  private JTextField myPathToAdd;
  private JCheckBox myUseGetQ;
  private JPanel mySettingsPanel;
  private JCheckBox myOpenNotebookCheckBox;


  @Override
  protected void resetEditorFrom(final MathematicaPackageRunConfiguration s) {

    final boolean useExisting = s.isOpenExistingNotebook();
    myOpenNotebookCheckBox.setSelected(useExisting);

    myNotebookSelector.setEnabled(useExisting);
    myNotebookSelector.removeAllItems();
    for (VirtualFile file : s.getNotebookFilesOfProject()) {
      myNotebookSelector.addItem(file);
      if (file.equals(s.getNotebookToOpen())) {
        myNotebookSelector.setSelectedItem(file);
      }
    }

    myLoadPackage.setText(s.getPackageContextToLoad());
    myPathToAdd.setText(s.getPathToAdd());
    myUseGetQ.setEnabled(s.isUseGetQ());

  }

  @Override
  protected void applyEditorTo(final MathematicaPackageRunConfiguration s) throws ConfigurationException {

    final boolean openExisting = myOpenNotebookCheckBox.isSelected();
    s.setOpenExistingNotebook(openExisting);
    s.setNotebookToOpen(null);

    final Object notebook = myNotebookSelector.getSelectedItem();
    if (notebook instanceof VirtualFile) {
      s.setNotebookToOpen((VirtualFile) notebook);
    }

    s.setPathToAdd(myPathToAdd.getText());
    s.setPackageContextToLoad(myLoadPackage.getText());
    s.setUseGetQ(myUseGetQ.isSelected());
  }

  @Override
  protected void disposeEditor() {
    mySettingsPanel.setVisible(false);
  }

  @NotNull
  @Override
  protected JComponent createEditor() {
    myNotebookSelector.setRenderer(new ListCellRendererWrapper<VirtualFile>() {
      @Override
      public void customize(final JList list, final VirtualFile value, final int index, final boolean selected, final boolean hasFocus) {
        setText(value.getName());
        setToolTipText(value.getPresentableUrl());
      }
    });

    myOpenNotebookCheckBox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        myNotebookSelector.setEnabled(myOpenNotebookCheckBox.isSelected());
      }
    });
    return mySettingsPanel;
  }


}
