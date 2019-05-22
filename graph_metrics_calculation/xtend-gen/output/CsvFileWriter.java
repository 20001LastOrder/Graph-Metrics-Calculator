package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class CsvFileWriter {
  public static void write(final ArrayList<ArrayList<String>> datas, final String uri) {
    int _size = datas.size();
    boolean _lessEqualsThan = (_size <= 0);
    if (_lessEqualsThan) {
      return;
    }
    try {
      File _file = new File(uri);
      final PrintWriter writer = new PrintWriter(_file);
      final StringBuilder output = new StringBuilder();
      for (final List<String> datarow : datas) {
        {
          for (int i = 0; (i < (datarow.size() - 1)); i++) {
            String _get = datarow.get(i);
            String _plus = (_get + ",");
            output.append(_plus);
          }
          int _size_1 = datarow.size();
          boolean _greaterThan = (_size_1 > 1);
          if (_greaterThan) {
            int _size_2 = datarow.size();
            int _minus = (_size_2 - 1);
            output.append(datarow.get(_minus));
            output.append("\n");
          }
        }
      }
      writer.write(output.toString());
      writer.close();
    } catch (final Throwable _t) {
      if (_t instanceof FileNotFoundException) {
        final FileNotFoundException e = (FileNotFoundException)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
}
