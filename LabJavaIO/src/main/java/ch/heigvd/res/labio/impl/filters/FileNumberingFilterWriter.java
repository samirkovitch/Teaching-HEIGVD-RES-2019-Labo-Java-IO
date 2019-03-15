package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  private int cptLine = 1;
  private boolean firstPassed = false;
  private boolean linePassed = false;
  private String tmp = "";
  private String carLign;
  private boolean mac = false; // permet de vérifier que le saut de ligne ne soit pas un saut de ligne windows (\r\n)

  @Override
  public void write(String str, int off, int len) throws IOException {

    for(int i = off; i < off + len;++i){
      write(str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for(int i = off; i < off + len;++i){
      write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    // 1ère ligne ?
    if (!firstPassed){
      tmp = cptLine + "\t";
      firstPassed = true;
    }
    // Regarde si c'est un saut de ligne linux
    // Si on a eu un saut de ligne mac avant, il passe au prochain test
    else if (c == '\n' && !linePassed && !mac){
      carLign = "\n"; // pour Linux
      tmp = carLign + (++cptLine) + "\t";
      linePassed = true;
    }
    // Regarde si c'est un saut de ligne mac
    else if ((c == '\r' && !linePassed) || mac){
      // Contrôle si ce n'est pas finalement un saut windows
      if (c == '\n'){
        carLign = "\r\n"; // pour Windows
        tmp = carLign + (++cptLine) + "\t";
        linePassed = true;
        mac =false;
      }
      // Rencontre d'un saut mac
      // On doit vérifier le caractère suivant pour savoir si ce n'est pas un saut windows
      // (mise en attente de l'écriture)
      else if (!mac){
        carLign = "\r"; // pour Mac
        mac = true; // bool pour mettre en attente l'écriture
      }
      else {
        tmp = carLign + (++cptLine) + "\t" + (char)c;
        linePassed = true;
        mac = false;
      }
    }
    // Aucun saut de ligne rencontré
    else{
      linePassed = false;
    }

    if (!linePassed) {
      tmp += (char) c;
    }

    // Si un saut mac est rencontré, on met en attente l'écriture
    if(!mac) {
      out.write(tmp);
      tmp = "";
    }
  }

}
