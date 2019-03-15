package ch.heigvd.res.labio.impl.explorers;

import ch.heigvd.res.labio.interfaces.IFileExplorer;
import ch.heigvd.res.labio.interfaces.IFileVisitor;
import java.io.File;
import java.util.Arrays;

/**
 * This implementation of the IFileExplorer interface performs a depth-first
 * exploration of the file system and invokes the visitor for every encountered
 * node (file and directory). When the explorer reaches a directory, it visits all
 * files in the directory and then moves into the subdirectories.
 * 
 * @author Olivier Liechti
 */
public class DFSFileExplorer implements IFileExplorer {

  @Override
  public void explore(File rootDirectory, IFileVisitor vistor) {
    vistor.visit(rootDirectory);

    // Récupère tous les fichiers du répertoire
    File[] files = rootDirectory.listFiles();

    // Si le répertoire ne contient pas de fichier
    if(files == null) {
      return;
    }
    else {
      // Trie les fichiers par ordre alphabétique
      Arrays.sort(files);

      // Regarde si le fichier n'est pas un répertoire
      for (File file : files) {
        explore(file, vistor);
      }
    }
  }

}
