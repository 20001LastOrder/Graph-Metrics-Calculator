package graph

import java.util.ArrayList
import java.util.List
import metrics.Metric
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference

class EMFGraph {
	val statistic = new GraphStatistic();
	var List<Metric> metrics;
	var String name;
	var String metaModel;
	
	static val String META_MODEL_HEADER = "Meta Mode"
	static val String NUM_NODE_HEADER = "Number Of Nodes";
	static val String NUM_EDGE_TYPE_HEADER = "Number of Edge types"; 
	
	
	def void init(EObject root, List<Metric> metrics, String name, List<EReference> referenceTypes){
		val otherContents = root.eAllContents.toList();
		otherContents.add(root);
		init(otherContents, metrics, name, referenceTypes);
	}
	
	/**
	 * init the graph with all nodes and reference types in the meta model
	 * @param objects: objects in the instance model (exclude root)
	 * @param metrics: metrics to be evaluated
	 * @param name: name of the instance model
	 * @param ReferenceTypes: reference types defined in the meta model
	 */
	def void init(List<EObject> objects, List<Metric> metrics, String name, List<EReference> referenceTypes){
		objects.forEach[it|
			statistic.addNode(it);
		]
		
		referenceTypes.forEach[it|
			statistic.addType(it.name);
		];
		
		objects.forEach[source|
			source.eClass.EAllReferences.forEach[r|
				//add the type first (if it is not added already)
				//many references
				if(r.isMany){
					source.getNeighbours(r).forEach[target|
						addEdge(source, target, r);
					]
				}else{
					//single references
					val target = source.eGet(r) as EObject;
					addEdge(source, target, r);
				}
			]
		]
		
		this.metrics = metrics;
		this.name = name;
	}
	
	/**
	 * evaluate all metrics for this model
	 * return the result as a two dimentional list
	 */
	def ArrayList<ArrayList<String>> evaluateAllMetrics(){
		val result = new ArrayList<ArrayList<String>>();
		setBasicInformation(result);
		
		for(metric : this.metrics){
			val datas = metric.evaluate(this.statistic);
			for(row : datas){
				result.add(new ArrayList<String>(row));
			}
		}
		return result;
	}
	
	/**
	 * Set basic information for the output
	 */
	private def setBasicInformation(ArrayList<ArrayList<String>> output){
		val metaInfo = new ArrayList<String>();
		metaInfo.add(META_MODEL_HEADER);
		metaInfo.add(this.metaModel);
		
		val edgeInfo = new ArrayList<String>();
		edgeInfo.add(NUM_EDGE_TYPE_HEADER);
		edgeInfo.add(this.statistic.allTypes.size()+"");
		
		val nodeInfo = new ArrayList<String>();
		nodeInfo.add(NUM_NODE_HEADER);
		nodeInfo.add(this.statistic.allNodes.size()+"");
		
		output.add(metaInfo);
		output.add(edgeInfo);
		output.add(nodeInfo);
	}
	
	def EList<EObject> getNeighbours(EObject o, EReference r){
		return (o.eGet(r, true) as EList<EObject>);
	}
	
	def addEdge(EObject source, EObject target, EReference r){
		if(target !== null && r !== null){
			statistic.addEdge(source, target, r.name);
		}
	}
	
	def GraphStatistic getStatistic(){
		return this.statistic;
	}
	
	def String getName(){
		return this.name;
	}
	
	def void setMetaModel(String model){
		this.metaModel = model;
	}
	
	def String getMetaModel(){
		return this.metaModel;
	}
	
}