# Simple script to extract some statistics from ErrorRecoveryBenchmark statistics
import pandas as pd
data = pd.read_csv("D:/stats/benchmark-stats-2024-11-26-0-5120.txt")
print(data.describe())

print("Duration stats:");
print(data[data["result"] == "recovery"]["duration"].describe())

print("Disambiguation duration:")
print(data[data["result"] == "recovery"]["disambiguationDuration"].describe())
