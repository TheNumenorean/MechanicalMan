package mechanicalman.flowchart.program;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Limits to MM files
 * @author Francesco Macagno
 *
 */
public class MMFileFilter extends FileFilter {
	
	@Override
	public boolean accept(File file) {
		return file.isDirectory() ? true : file == null ? false : file.getName().endsWith(".mm");
	}

	@Override
	public String getDescription() {
		return "*.mm";
	}
	
}
