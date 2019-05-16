package input;

import graph.EMFGraph;
import hu.bme.mit.inf.dslreasoner.workspace.FileSystemWorkspace;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import metrics.Metric;
import metrics.MultiplexParticipationCoefficientMetric;
import metrics.NodeActivityMetric;
import metrics.OutDegreeMetric;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class GraphReader {
  public static void init() {
    Map<String, Object> _extensionToFactoryMap = Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap();
    XMIResourceFactoryImpl _xMIResourceFactoryImpl = new XMIResourceFactoryImpl();
    _extensionToFactoryMap.put("*", _xMIResourceFactoryImpl);
  }
  
  public static List<EMFGraph> readModels(final String path) {
    try {
      final File dir = new File(path);
      boolean _isDirectory = dir.isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        throw new Exception("expecting a directory");
      }
      final ArrayList<EMFGraph> graphs = new ArrayList<EMFGraph>();
      final FileSystemWorkspace workspace = new FileSystemWorkspace(path, "");
      final ArrayList<Metric> metrics = new ArrayList<Metric>();
      OutDegreeMetric _outDegreeMetric = new OutDegreeMetric();
      metrics.add(_outDegreeMetric);
      NodeActivityMetric _nodeActivityMetric = new NodeActivityMetric();
      metrics.add(_nodeActivityMetric);
      MultiplexParticipationCoefficientMetric _multiplexParticipationCoefficientMetric = new MultiplexParticipationCoefficientMetric();
      metrics.add(_multiplexParticipationCoefficientMetric);
      final Function1<String, Boolean> _function = (String it) -> {
        return Boolean.valueOf(it.endsWith(".xmi"));
      };
      Iterable<String> _filter = IterableExtensions.<String>filter(((Iterable<String>)Conversions.doWrapArray(dir.list())), _function);
      for (final String name : _filter) {
        {
          final File file = new File(name);
          final EObject model = workspace.<EObject>readModel(EObject.class, file.getName());
          final EMFGraph g = new EMFGraph();
          g.init(model, metrics, name.replaceFirst(".xmi", ""));
          graphs.add(g);
        }
      }
      return graphs;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
