import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import seaborn as sns
import stats_support as stats

# %% [markdown]
# Prepare data

# %%
data = stats.readData("benchmark-stats-2025-04-19-parent-amb-3-rascal-0-10240.txt.gz")
recovered = data[data["result"] == "recovery"].sort_values("size")
range = np.arange(0, 11264, 1024)
recovered["bsize"] = pd.cut(recovered["size"], range, labels=range[1:])

recovered_grouped = recovered.groupby(['bsize']).max()

# %% [markdown]
# Prepare graphs

# %%
sns.set_theme()
fig1, ax1 = plt.subplots(figsize=(8, 6))

sns.violinplot(
    data=recovered,
    x="bsize", y="duration",
    density_norm="width",
    ax = ax1
)
plt.show()

# %% [markdown]
# Visualize the recovery duration per file size bucket using a violing graph.

# %% [markdown]
# The node distribution per file size

# %%
fig2, ax2 = plt.subplots(figsize=(8, 6))
ax2.set_yscale('log')
sns.violinplot(
    data=recovered,
    x="bsize", y="nodes",
    density_norm="width",
    ax=ax2
)
plt.show()


