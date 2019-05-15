package output;

import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.util.List

class CsvFileWriter {
	def static void write(List<CsvData> datas, String uri) {
		if(datas.size() <= 0) {
			return;
		}
		
		println("Output csv for " + uri);
		try  {
			val PrintWriter writer = new PrintWriter(new File(uri));
			val output = new StringBuilder;
			output.append(datas.get(0).fieldNames());
			
			for(CsvData data : datas) {
				output.append(data.outputData());
			}
			
			writer.write(output.toString());
			writer.close();
			println("Output csv finished");
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
