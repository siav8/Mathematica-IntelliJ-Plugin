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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author patrick (15.01.15)
 */
public class MathematicaPackageRunConfiguration extends RunConfigurationBase {

  private String myPackageContextToLoad = "";
  private String myPathToAdd = "";

  private boolean myOpenExistingNotebook = false;
  private VirtualFile myNotebookToOpen = null;
  private boolean myUseGetQ = true;

  private Project myProject;


  protected MathematicaPackageRunConfiguration(final Project project) {
    super(project, new MathematicaPackageConfigurationFactory(), "Run a Package");
    myProject = project;
    myPackageContextToLoad = project.getName() + "`";
    myPathToAdd = project.getBasePath();
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {

  }

  public Collection<VirtualFile> getNotebookFilesOfProject() {
    final Collection<VirtualFile> allFiles = FilenameIndex.getAllFilesByExt(myProject, "nb");
    Collection<VirtualFile> result = new ArrayList<VirtualFile>(allFiles.size());
    for (VirtualFile file : allFiles) {
      result.add(file);
    }
    return result;
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new MathematicaRunPackageEditor();
  }

  @Nullable
  @Override
  public RunProfileState getState(@NotNull final Executor executor, @NotNull final ExecutionEnvironment environment) throws ExecutionException {
    return null;
  }

  public void setPackageContextToLoad(@NotNull final String context) {
    this.myPackageContextToLoad = context;
  }

  public void setPathToAdd(@NotNull final String path) {
    this.myPathToAdd = path;
  }

  public void setNotebookToOpen(final VirtualFile notebook) {
    this.myNotebookToOpen = notebook;
  }

  public void setUseGetQ(final boolean useGetQ) {
    this.myUseGetQ = useGetQ;
  }

  @NotNull
  public String getPackageContextToLoad() {
    return myPackageContextToLoad;
  }

  @NotNull
  public String getPathToAdd() {
    return myPathToAdd;
  }

  public VirtualFile getNotebookToOpen() {
    return myNotebookToOpen;
  }

  public boolean isUseGetQ() {
    return myUseGetQ;
  }

  public boolean isOpenExistingNotebook() {
    return myOpenExistingNotebook;
  }

  public void setOpenExistingNotebook(final boolean openExistingNotebook) {
    this.myOpenExistingNotebook = openExistingNotebook;
  }
}
