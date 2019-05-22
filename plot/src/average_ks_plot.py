from GraphType import GraphType
from scipy import stats
import numpy as np

def average_ks_distance(targets, sample):
    distance = 0.0
    for target in targets:
        distance += stats.ks_2samp(target, sample)
    
    distance = distance / len(targets)
    return distance



# human = GraphType('../statistics/humanOutput/', 500, 'Human')
viatraEvolve = GraphType('../statistics/viatraEvolve/', 1, 'viatraEvolve', shouldShuffle = False)
