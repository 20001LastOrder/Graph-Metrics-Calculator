package input

import graph.EMFGraph
import hu.bme.mit.inf.dslreasoner.workspace.FileSystemWorkspace
import java.io.File
import java.util.ArrayList
import java.util.List
import metrics.Metric
import metrics.NodeActivityMetric
import metrics.OutDegreeMetric
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import metrics.MultiplexParticipationCoefficientMetric

class GraphReader{
	static def void init() {
		Resource.Factory.Registry.INSTANCE.extensionToFactoryMap.put("*",new XMIResourceFactoryImpl)
	}
	
	static def List<EMFGraph> readModels(String path){
		val dir = new File(path);
		if(!dir.isDirectory){
			throw new Exception("expecting a directory");
		}
		
		val graphs = new ArrayList<EMFGraph>();
		val workspace = new FileSystemWorkspace(path,"");
		
		val metrics = new ArrayList<Metric>();
		metrics.add(new OutDegreeMetric());
		metrics.add(new NodeActivityMetric());
		metrics.add(new MultiplexParticipationCoefficientMetric());
				
		//check all files in the directory with xmi
		for(String name : dir.list.filter[it| it.endsWith(".xmi")]){
			val file = new File(name);
			val model = workspace.readModel(EObject, file.name);			
			//add a list of metrics
			val g = new EMFGraph();
			g.init(model, metrics, name.replaceFirst(".xmi", ""));
			graphs.add(g);
		}
		
		return graphs;
	}
}