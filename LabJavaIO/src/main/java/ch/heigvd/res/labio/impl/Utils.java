package ch.heigvd.res.labio.impl;

import java.util.logging.Logger;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {
    // Il n'y a pas de jeu de mot par rapport au cours
    String res[] = new String[2];
    res[0] = "";

    // Parcout la ligne afin de trouver le prochain saut
    for (int i = 0; i < lines.length(); ++i) {
      // Si il y a un saut
      if (lines.charAt(i) == '\n' || lines.charAt(i) == '\r') {
        // Vérifie si ce n'est pas un saut windows
        if (lines.charAt(i) == '\r' && i + 1 < lines.length() && lines.charAt(i + 1) == '\n') {
          ++i;
        }

        res[0] = lines.substring(0, i + 1);
        res[1] = lines.substring(i + 1);
        break;
      }
    }

    if(res[0].equals("")){
      res[1] = lines;
    }

    return res;
  }

}
