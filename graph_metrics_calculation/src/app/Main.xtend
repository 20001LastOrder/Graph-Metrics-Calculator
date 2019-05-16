package app

import graph.EMFGraph
import hu.bme.mit.inf.dslreasoner.domains.yakindu.sgraph.yakindumm.impl.YakindummPackageImpl
import input.GraphReader
import java.util.ArrayList
import output.CsvFileWriter
import socialnetwork.impl.SocialnetworkPackageImpl

class Main {
	def static void main(String[] args){
		GraphReader.init();
		//init model
		SocialnetworkPackageImpl.eINSTANCE.eClass;
		YakindummPackageImpl.eINSTANCE.eClass;
		
		println("Start Reading Models...");
		val models = new ArrayList<EMFGraph>();
		
		for(var i = 1; i <= 20; i++){
			models.addAll(GraphReader.readModels("viatraInput/VS+i/models/" + "run"+i));
			models.addAll(GraphReader.readModels("viatraInput/VS-i/models/" + "run"+i));
		}
		
		
		
		println("Reading Models Ended")
		
		for(model : models){
			println("evaluating for " + model.name);
			model.metaModel = YakindummPackageImpl.eNAME;
			CsvFileWriter.write(model.evaluateAllMetrics(), "viatraOutput/"+model.name+".csv");
		}
		println("finished");
	}
}