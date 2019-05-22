import readCSV as reader

class GraphType:
    
    # init with path contrain files and number of files to read reader is imported from (readCSV)
    def __init__(self, path, number, name, shouldShuffle = True):
        self.out_ds = []
        self.nas = []
        self.mpcs = []
        self.name = name
        models = reader.readmultiplefiles(path, number, shouldShuffle)
        for i in range(len(models)):
            out_d, na, mpc = reader.getmetrics(models[i])
            self.out_ds.append(out_d)
            self.nas.append(na)
            self.mpcs.append(mpc)