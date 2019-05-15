package graph

import java.util.Iterator
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference

class EMFGraph {
	val statistic = new GraphStatistic();
	
	def void init(EObject root){
		init(root.eAllContents());
	}
	
	def void init(Iterator<EObject> container){
		val objects = container.toList();
		objects.forEach[it|
			statistic.addNode(it);
		]
		
		objects.forEach[source|
			source.eClass.EReferences.forEach[r|
				source.getNeighbours(r).forEach[target|
					addEdge(source, target, r);
				]
			]
		]
		
	}
	
	def EList<EObject> getNeighbours(EObject o, EReference r){
		return (o.eGet(r, true) as EList<EObject>);
	}
	
	def addEdge(EObject source, EObject target, EReference r){
		if(target !== null && r !== null){
			statistic.addEdge(source, target, r.name);
		}
	}
	
}