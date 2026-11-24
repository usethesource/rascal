# Rascal Developer Manual

## Working with the repository

The Rascal project makes use of Java code that is generated from Rascal modules.
To avoid pollution of the Rascal repository itself, the generated code is not committed to this repository, but to a [separate repository](https://github.com/usethesource/rascal-generated-java).
This secondary repository is "linked in" using Git's submodule system, in the `generated` directory in the root of the repository.

To start working on Rascal, check out the Rascal repository (`git clone --recurse-submodules git@github.com:usethesource/rascal.git`).
Note: for existing (local) repositories, initialize the submodule through `git submodule update --init`.

By default, `git` operations (such as `git pull`) will reel in changes to submodules, but will not actually alter the content of submodule directories.
To bring the contents up to date, run `git submodule update` after a pull, or add the `--recurse-submodules` to `git pull`.
To automate this, set the configuration option `submodule.recurse` to `true` for the Rascal project.
