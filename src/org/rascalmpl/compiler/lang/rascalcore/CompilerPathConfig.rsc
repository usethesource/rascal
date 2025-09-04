module lang::rascalcore::CompilerPathConfig

extend util::PathConfig;

data PathConfig(
    loc generatedSources       = |unknown:///|,
    loc generatedTestSources   = |unknown:///|,
    loc generatedResources     = |unknown:///|,
    loc generatedTestResources = |unknown:///|
);