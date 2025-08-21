package org.rascalmpl.util.maven;

import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.junit.Test;

public class ArtifactTest {
    @Test
    public void testIncompatibleRanges() throws InvalidVersionSpecificationException {
        VersionRange range1 = VersionRange.createFromVersionSpec("[1.0,2.0]");
        VersionRange range2 = VersionRange.createFromVersionSpec("[3.0,4.0]");
        VersionRange range3 = VersionRange.createFromVersionSpec("[2.0,3.0]");
        VersionRange merged = range1.restrict(range2);
        System.out.println("range1: " + range1 + ", range2: " + range2 + ", merged: " + merged);
        System.out.println("restrictions: " + merged.getRestrictions());
        System.out.println("equals: " + range1.equals(VersionRange.createFromVersion("[]")));
        System.out.println("recommended: " + merged.getRecommendedVersion());
        System.out.println("recommended 1: " + range1.getRecommendedVersion());
        System.out.println("recommended 2: " + range2.getRecommendedVersion());

        VersionRange merged2 = range1.restrict(range3);
        System.out.println("restrictions: " + merged2.getRestrictions());
        System.out.println("merged2: " + merged2);
    }
}
