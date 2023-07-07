module lang::rascal::tests::library::util::SemVerTests

import util::SemVer;
import Exception;

test bool lessVersion1() = lessVersion("1.0.0", "2.0.0");
test bool lessVersion2() = lessVersion("2.0.0", "2.1.0");
test bool lessVersion3() = lessVersion("2.1.0", "2.1.1");
test bool lessVersion4() = lessVersion("1.0.0-alpha", "1.0.0");
test bool lessVersion5() = lessVersion("1.0.0-alpha", "1.0.0-alpha.1");
test bool lessVersion6() = lessVersion("1.0.0-alpha.1",  "1.0.0-alpha.beta");
test bool lessVersion7() = lessVersion("1.0.0-alpha.beta", "1.0.0-beta");
test bool lessVersion8() = lessVersion("1.0.0-beta",  "1.0.0-beta.2");
test bool lessVersion9() = lessVersion("1.0.0-beta.2", "1.0.0-beta.11");
test bool lessVersion10() = lessVersion("1.0.0-beta.11", "1.0.0-rc.1");
test bool lessVersion11() = lessVersion("1.0.0-rc.1",  "1.0.0");

test bool ge1() = satisfiesVersion("1.2.7", "\>=1.2.7");
test bool ge2() = satisfiesVersion("1.2.8", "\>=1.2.7");
test bool ge3() = satisfiesVersion("2.5.3", "\>=1.2.7");
test bool ge4() = satisfiesVersion("1.3.9", "\>=1.2.7");
test bool ge5() = !satisfiesVersion("1.2.6", "\>=1.2.7");
test bool ge6() = !satisfiesVersion("1.1.0", "\>=1.2.7");

test bool and1() = satisfiesVersion("1.2.7", "\>=1.2.7 \<1.3.0");
test bool and2() = satisfiesVersion("1.2.8", "\>=1.2.7 \<1.3.0");
test bool and3() = satisfiesVersion("1.2.99", "\>=1.2.7 \<1.3.0");
test bool and4() = !satisfiesVersion("1.2.6", "\>=1.2.7 \<1.3.0");
test bool and5() = !satisfiesVersion("1.3.0", "\>=1.2.7 \<1.3.0");
test bool and6() = !satisfiesVersion("1.1.0", "\>=1.2.7 \<1.3.0");

test bool or1() = satisfiesVersion("1.2.7", "1.2.7 || \>=1.2.9 \<2.0.0");
test bool or2() = satisfiesVersion("1.2.9", "1.2.7 || \>=1.2.9 \<2.0.0");
test bool or3() = satisfiesVersion("1.4.6", "1.2.7 || \>=1.2.9 \<2.0.0");
test bool or4() = !satisfiesVersion("1.2.8", "1.2.7 || \>=1.2.9 \<2.0.0");
test bool or5() = !satisfiesVersion("2.0.0", "1.2.7 || \>=1.2.9 \<2.0.0");

test bool prerelease1() = satisfiesVersion("1.2.3-alpha.7", "\>1.2.3-alpha.3");
test bool prerelease2() = satisfiesVersion("1.2.3-alpha.9", "\>1.2.3-alpha.3");
test bool prerelease3() = satisfiesVersion("3.4.5", "\>1.2.3-alpha.3");

test bool hyphen1() = satisfiesVersion("1.2.3", "1.2.3 - 2.3.4");
test bool hyphen2() = satisfiesVersion("2.2.3", "1.2.3 - 2.3.4");
test bool hyphen3() = !satisfiesVersion("2.3.5", "1.2.3 - 2.3.4");
test bool hyphen4() = satisfiesVersion("1.2.3", "1.2 - 2.3.4");
test bool hyphen5() = satisfiesVersion("1.2.3", "1.2 - 2.3");
test bool hyphen6() = satisfiesVersion("2.2.99", "1.2 - 2.3");
test bool hyphen7() = satisfiesVersion("2.3.0", "1.2 - 2.3");
test bool hyphen8() = !satisfiesVersion("2.3.1", "1.2 - 2.3");
test bool hyphen9() = satisfiesVersion("1.2.3", "1.2 - 2");
test bool hyphen10() = !satisfiesVersion("2.3.1", "1.2 - 2");
test bool hyphen11() = !satisfiesVersion("3", "1.2 - 2");

test bool x1() = satisfiesVersion("1.2.3", "*");
test bool x2() = satisfiesVersion("1.2.3", "x");
test bool x3() = satisfiesVersion("1.2.3", "1.*");
test bool x4() = !satisfiesVersion("2.2.3", "1.*");
test bool x5() = satisfiesVersion("1.2.3", "1.2.*");
test bool x6() = !satisfiesVersion("1.3.3", "1.2.*");

test bool tilde1() = satisfiesVersion("1.2.3", "~1.2.3");
test bool tilde2() = satisfiesVersion("1.2.9", "~1.2.3");
test bool tilde3() = !satisfiesVersion("1.3.0", "~1.2.3");
test bool tilde4() = satisfiesVersion("1.2.0", "~1.2");
test bool tilde5() = satisfiesVersion("1.2.9", "~1.2");
test bool tilde6() = !satisfiesVersion("1.3.0", "~1.2");
test bool tilde7() = satisfiesVersion("1.0.0", "~1");
test bool tilde8() = satisfiesVersion("1.2.3", "~1");
test bool tilde9() = !satisfiesVersion("2.0.0", "~1");
test bool tilde10() = satisfiesVersion("0.2.3", "~0.2.3");
test bool tilde11() = satisfiesVersion("0.2.9", "~0.2.3");
test bool tilde12() = !satisfiesVersion("0.3.0", "~0.2.3");
test bool tilde13() = satisfiesVersion("0.2.0", "~0.2");
test bool tilde14() = satisfiesVersion("0.2.9", "~0.2");
test bool tilde15() = !satisfiesVersion("0.3.0", "~0.2");
test bool tilde16() = satisfiesVersion("0.0.0", "~0");
test bool tilde17() = satisfiesVersion("0.1.2", "~0");
test bool tilde18() = !satisfiesVersion("1.0.0", "~0");
test bool tilde19() = satisfiesVersion("1.2.3-beta.2", "~1.2.3-beta.2");
test bool tilde20() = satisfiesVersion("1.2.4", "~1.2.3-beta.2");
test bool tilde21() = !satisfiesVersion("1.3.0", "~1.2.3-beta.2");
test bool tilde22() = satisfiesVersion("1.2.3-beta.3", "~1.2.3-beta.2");
test bool tilde23() = satisfiesVersion("1.2.4-beta.2", "~1.2.3-beta.2");

test bool caret1() = satisfiesVersion("1.2.3", "^1.2.3");
test bool caret2() = satisfiesVersion("1.4.9", "^1.2.3");
test bool caret3() = !satisfiesVersion("2.0.0", "^1.2.3");
test bool caret4() = satisfiesVersion("0.2.3", "^0.2.3");
test bool caret5() = satisfiesVersion("0.2.9", "^0.2.3");
test bool caret6() = !satisfiesVersion("0.3.0", "^0.2.3");
test bool caret7() = satisfiesVersion("0.0.3", "^0.0.3");
test bool caret8() = !satisfiesVersion("0.0.4", "^0.0.3");
test bool caret9() = satisfiesVersion("1.2.3-beta.2", "^1.2.3-beta.2");
test bool caret10() = !satisfiesVersion("2.0.0", "^1.2.3-beta.2");
test bool caret11() = satisfiesVersion("1.2.3-beta.4", "^1.2.3-beta.2");
test bool caret12() = satisfiesVersion("1.2.4-beta.2", "^1.2.3-beta.2");
test bool caret13() = satisfiesVersion("0.0.3-beta", "^0.0.3-beta");
test bool caret14() = !satisfiesVersion("0.0.4", "^0.0.3-beta");
test bool caret15() = satisfiesVersion("0.0.3-pr.2", "^0.0.3-beta");
test bool caret16() = satisfiesVersion("1.2.0", "^1.2.x");
test bool caret17() = satisfiesVersion("1.2.5", "^1.2.x");
test bool caret18() = !satisfiesVersion("2.0.0", "^1.2.x");
test bool caret19() = satisfiesVersion("0.0.0", "^0.0.x");
test bool caret20() = satisfiesVersion("0.0.5", "^0.0.x");
test bool caret21() = !satisfiesVersion("0.1.0", "^0.0.x");
test bool caret22() = satisfiesVersion("0.0.0", "^0.0");
test bool caret23() = satisfiesVersion("0.0.5", "^0.0");
test bool caret24() = !satisfiesVersion("0.1.0", "^0.0");
test bool caret25() = satisfiesVersion("1.0.0", "^1.x");
test bool caret26() = satisfiesVersion("1.5.3", "^1.x");
test bool caret27() = !satisfiesVersion("2.0.0", "^1.x");
test bool caret28() = satisfiesVersion("0.0.0", "^0.x");
test bool caret29() = satisfiesVersion("0.5.3", "^0.x");
test bool caret30() = !satisfiesVersion("1.0.0", "^0.x");

@expected{
IllegalArgument
}
test bool invalid1() = lessVersion("a.1.2", "b.1.2");

@expected{
IllegalArgument
}
test bool invalid2() = lessVersion("1;1.2", "1;1.2");

@expected{
IllegalArgument
}
test bool invalid3() = satisfiesVersion("0.5.3", "^0.y");

@expected{
IllegalArgument
}
test bool invalid4() = satisfiesVersion("0.5.3", "@0.y");

@expected{
IllegalArgument
}
test bool invalid5() = satisfiesVersion("0.5.3", "0.1 - 0.z");

@expected{
IllegalArgument
}
test bool invalid6() = satisfiesVersion("0.1", "0.1 z 0.2");

@expected{
IllegalArgument
}
test bool invalid7() = satisfiesVersion("0.5.3", "\> =0.1");

@expected{
IllegalArgument
}
test bool invalid8() = satisfiesVersion("0.5.3", "\>= 0.1");
