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
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import de.halirutan.mathematica.sdk.MathematicaSdkType;
import org.jetbrains.annotations.NotNull;

import static de.halirutan.mathematica.MathematicaBundle.message;

/**
 * @author patrick (29.01.15)
 */

public class MathematicaRunState extends CommandLineState {
  private final Project myProject;

  protected MathematicaRunState(final ExecutionEnvironment environment) {
    super(environment);
    myProject = environment.getProject();
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    final GeneralCommandLine commandline = getCommandline();
    return new OSProcessHandler(commandline.createProcess(), commandline.getCommandLineString());
  }

  private GeneralCommandLine getCommandline() throws ExecutionException {
    GeneralCommandLine cmd = new GeneralCommandLine();
    setWorkingDirectory(cmd);
    setMathematicaExec(cmd);
    return cmd;
  }

  private void setMathematicaExec(final GeneralCommandLine cmd) throws ExecutionException {
    final Sdk projectSdk = ProjectRootManager.getInstance(myProject).getProjectSdk();
    if (projectSdk == null || projectSdk.getSdkType() != MathematicaSdkType.getInstance()) {
      throw new ExecutionException(message("sdk.not.defined"));
    }
    if (projectSdk.getHomePath() == null) {
      throw new ExecutionException(message("sdk.no.home.path"));
    }

    String executablePath;
    // TODO: Implement this for Mac and Windows
    if (SystemInfo.isLinux) {
      executablePath = FileUtil.join(projectSdk.getHomePath(), "Executables", "Mathematica");
    } else {
      executablePath = FileUtil.join(projectSdk.getHomePath(), "Executables", "Mathematica");
    }

    cmd.setExePath(FileUtil.toSystemDependentName(executablePath));
  }

  private void setWorkingDirectory(final GeneralCommandLine cmd) {
    final String dir = FileUtil.toSystemDependentName(myProject.getBaseDir().getPath());
    cmd.withWorkDirectory(dir);
  }




}