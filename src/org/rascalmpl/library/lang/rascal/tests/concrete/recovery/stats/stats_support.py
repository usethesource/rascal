
import pandas as pd
import numpy as np
import gzip

statDir = "D:/stats/"

def readData(file):
    dtypes={
        'nodes':float,
        'prune0': float,'prune1': float,'prune2': float,'prune3': float,'prune4': float,'prune5': float,'prune6': float,'prune7': float,'prune8': float,'prune9': float
    }

    path = statDir + file
    if (file.endswith(".gz")):
        fd = gzip.open(path, 'rb')
    else:
        fd = open(path, 'rb')

    data = pd.read_csv(fd, dtype=dtypes)

    recovered = data[data["result"] == "recovery"].sort_values("size")
    range = np.arange(0, 11264, 1024)
    recovered["bsize"] = pd.cut(recovered["size"], range, labels=range[1:])

    return recovered
