module lang::rascalcore::CompilerPathConfig

import util::Reflective;
import util::PathConfig;

data PathConfig(
    loc generatedSources       = |unknown:///|,
    loc generatedTestSources   = |unknown:///|,
    loc generatedResources     = |unknown:///|,
    loc generatedTestResources = |unknown:///|
);