package metrics;

import graph.GraphStatistic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import metrics.Metric;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class OutDegreeMetric extends Metric {
  private static final String countName = "OutDegreeCount";
  
  private static final String valueName = "OutDegreeValue";
  
  @Override
  public String[][] evaluate(final GraphStatistic g) {
    final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    final Consumer<EObject> _function = (EObject it) -> {
      final int value = g.outDegree(it);
      boolean _containsKey = map.containsKey(Integer.valueOf(value));
      boolean _not = (!_containsKey);
      if (_not) {
        map.put(Integer.valueOf(value), Integer.valueOf(1));
      } else {
        Integer _get = map.get(Integer.valueOf(value));
        int _plus = ((_get).intValue() + 1);
        map.put(Integer.valueOf(value), Integer.valueOf(_plus));
      }
    };
    g.getAllNodes().forEach(_function);
    int _size = map.size();
    int _plus = (_size + 1);
    final String[][] datas = new String[3][_plus];
    datas[0][0] = OutDegreeMetric.valueName;
    datas[1][0] = OutDegreeMetric.countName;
    int count = 1;
    final Function1<Map.Entry<Integer, Integer>, Integer> _function_1 = (Map.Entry<Integer, Integer> it) -> {
      return it.getKey();
    };
    List<Map.Entry<Integer, Integer>> _sortBy = IterableExtensions.<Map.Entry<Integer, Integer>, Integer>sortBy(map.entrySet(), _function_1);
    for (final Map.Entry<Integer, Integer> entry : _sortBy) {
      {
        Integer _key = entry.getKey();
        String _plus_1 = (_key + "");
        datas[0][count] = _plus_1;
        Integer _value = entry.getValue();
        String _plus_2 = (_value + "");
        datas[1][count] = _plus_2;
        count++;
      }
    }
    return datas;
  }
}
