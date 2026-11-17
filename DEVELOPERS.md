# Rascal Developer Manual

## Checking out the repository

The Rascal project makes use of Java code that is generated from Rascal modules.
To avoid pollution of the Rascal repository itself, the generated code is not committed to this repository, but to a [separate repository](https://github.com/usethesource/rascal-generated-java).
This secondary repository is "linked in" using Git's submodule system, in the `generated` directory in the root of the repository.

To start working on Rascal, execute the following steps.
1. Check out the Rascal repository (e.g., `git clone git@github.com:usethesource/rascal.git`).
2. Initialize the submodule (`git submodule update --init`). The `generated` directory should now be filled.

## Updating the submodule

After an update of the submodule's commit pointer, `git pull` will (by default) not update the submodule directory. To update the submodule, run `git submodule update`.
