package output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import output.CsvData;

@SuppressWarnings("all")
public class CsvFileWriter {
  public static void write(final List<CsvData> datas, final String uri) {
    int _size = datas.size();
    boolean _lessEqualsThan = (_size <= 0);
    if (_lessEqualsThan) {
      return;
    }
    InputOutput.<String>println(("Output csv for " + uri));
    try {
      File _file = new File(uri);
      final PrintWriter writer = new PrintWriter(_file);
      final StringBuilder output = new StringBuilder();
      output.append(datas.get(0).fieldNames());
      for (final CsvData data : datas) {
        output.append(data.outputData());
      }
      writer.write(output.toString());
      writer.close();
      InputOutput.<String>println("Output csv finished");
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
