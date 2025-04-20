# Simple script to extract some statistics from ErrorRecoveryBenchmark statistics
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import seaborn as sns
import pickle
import gzip


print("Pandas version: ", pd.__version__)


dtypes={
    'nodes':float,
    'prune0': float,'prune1': float,'prune2': float,'prune3': float,'prune4': float,'prune5': float,'prune6': float,'prune7': float,'prune8': float,'prune9': float
}

dir = "D:/stats/"
#file = "benchmark-stats-2025-04-09-amb-pruning-rascal-0-10240";
file = "benchmark-stats-2025-04-19-parent-amb-3-rascal-0-5120";

with gzip.open(dir + file + ".txt.gz", 'rb') as f:
    data = pd.read_csv(f, dtype=dtypes)
    #data = pickle.load(f, encoding='latin1')
#data = pd.read_csv(dir +  file + ".txt.gz",dtype=dtypes)
#data.to_pickle(dir +  + file + "".pkl")
#data = pd.read_pickle(dir + file + ".pkl")
print(data.describe())



recovered = data[data["result"] == "recovery"].sort_values("size")

print("Duration stats:");
print(recovered["duration"].describe())

print("Disambiguation duration:")
print(recovered["disambiguationDuration"].describe())

print("Nodes")
print(recovered["nodes"].describe(), flush=True)

print("Prune 0")
print(recovered["prune0"].describe(), flush=True)

print("Prune 6")
print(recovered["prune6"].describe(), flush=True)

print("unodes")
print(recovered["unodes"].describe(), flush=True)
#prune_plot = recovered.boxplot(column=["uprune0","uprune1","uprune2","uprune3","unodes"])

#prune_plot = recovered.boxplot(column=["prune0","prune1","prune2","prune3","prune4","prune5","prune6","prune7","prune8","prune9"])
sns.violinplot(
    data=recovered,
    x="bsize", y="duration",
    density_norm="width"
)
#prune_plot = recovered.boxplot(column=["prune0","prune1","prune2","prune3"])
#prune_plot.set_yscale('log')

#duration_plot = recovered.plot(style=".", y="duration",x="size",kind="scatter",logy=True)

range = np.arange(0, 11264, 1024)

recovered["bsize"] = pd.cut(recovered["size"], range, labels=range[1:])
#sns.scatterplot(data=recovered, x="size", y="duration")

recovered_filtered = recovered[recovered.duration <= 500]

recovered_slow = recovered[recovered.duration > 500]
print("Slow recovery stats:")
print(recovered_slow.describe())


size = recovered_filtered["size"]
duration = recovered_filtered["duration"]
#plt.scatter(recovered_filtered["size"], recovered_filtered["duration"], s=3)
#rolling_mean = recovered_filtered.rolling(window=100)["duration"].mean()
#plt.plot(rolling_mean)

recovered_filtered["bsize"] = pd.cut(recovered_filtered["size"], range, labels=range[1:])
'''
sns.violinplot(
    data=recovered_filtered,
    x="bsize", y="duration",
    density_norm="width"
)
'''

#plt.ylabel("Recovery parse time (ms.)")
#plt.xlabel("File size (bytes)")
ax = plt.gca()

plt.yscale('log')
plt.show()