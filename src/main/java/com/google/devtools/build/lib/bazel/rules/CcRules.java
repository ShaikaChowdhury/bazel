// Copyright 2018 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.google.devtools.build.lib.bazel.rules;

import com.google.common.collect.ImmutableList;
import com.google.devtools.build.lib.analysis.ConfiguredRuleClassProvider.Builder;
import com.google.devtools.build.lib.analysis.ConfiguredRuleClassProvider.RuleSet;
import com.google.devtools.build.lib.bazel.rules.CcToolchainType.CcToolchainTypeRule;
import com.google.devtools.build.lib.bazel.rules.cpp.BazelCcBinaryRule;
import com.google.devtools.build.lib.bazel.rules.cpp.BazelCcImportRule;
import com.google.devtools.build.lib.bazel.rules.cpp.BazelCcLibraryRule;
import com.google.devtools.build.lib.bazel.rules.cpp.BazelCcTestRule;
import com.google.devtools.build.lib.bazel.rules.cpp.BazelCppRuleClasses;
import com.google.devtools.build.lib.rules.core.CoreRules;
import com.google.devtools.build.lib.rules.cpp.CcImportRule;
import com.google.devtools.build.lib.rules.cpp.CcModule;
import com.google.devtools.build.lib.rules.cpp.CcToolchainAlias.CcToolchainAliasRule;
import com.google.devtools.build.lib.rules.cpp.CcToolchainRule;
import com.google.devtools.build.lib.rules.cpp.CcToolchainSuiteRule;
import com.google.devtools.build.lib.rules.cpp.CppBuildInfo;
import com.google.devtools.build.lib.rules.cpp.CppConfigurationLoader;
import com.google.devtools.build.lib.rules.cpp.CppOptions;
import com.google.devtools.build.lib.rules.cpp.CppRuleClasses.CcIncludeScanningRule;
import com.google.devtools.build.lib.rules.cpp.CpuTransformer;
import com.google.devtools.build.lib.rules.cpp.FdoProfileRule;
import com.google.devtools.build.lib.rules.platform.PlatformRules;

/**
 * Rules for C++ support in Bazel.
 */
public class CcRules implements RuleSet {
  public static final CcRules INSTANCE = new CcRules();

  private CcRules() {
    // Use the static INSTANCE field instead.
  }

  @Override
  public void init(Builder builder) {
    builder.addSkylarkAccessibleTopLevels("cc_common", CcModule.INSTANCE);

    builder.addConfig(CppOptions.class, new CppConfigurationLoader(CpuTransformer.IDENTITY));
    builder.addBuildInfoFactory(new CppBuildInfo());

    builder.addRuleDefinition(new CcToolchainRule());
    builder.addRuleDefinition(new CcToolchainSuiteRule());
    builder.addRuleDefinition(new CcToolchainAliasRule());
    builder.addRuleDefinition(new CcImportRule());
    builder.addRuleDefinition(new CcToolchainTypeRule());
    builder.addRuleDefinition(new BazelCppRuleClasses.CcLinkingRule());
    builder.addRuleDefinition(new BazelCppRuleClasses.CcDeclRule());
    builder.addRuleDefinition(new BazelCppRuleClasses.CcBaseRule());
    builder.addRuleDefinition(new BazelCppRuleClasses.CcRule(
        BazelRuleClassProvider.TOOLS_REPOSITORY + "//tools/def_parser:def_parser"));
    builder.addRuleDefinition(new BazelCppRuleClasses.CcBinaryBaseRule());
    builder.addRuleDefinition(new BazelCcBinaryRule());
    builder.addRuleDefinition(new BazelCcTestRule());
    builder.addRuleDefinition(new BazelCppRuleClasses.CcLibraryBaseRule());
    builder.addRuleDefinition(new BazelCcLibraryRule());
    builder.addRuleDefinition(new BazelCcImportRule());
    builder.addRuleDefinition(new CcIncludeScanningRule());
    builder.addRuleDefinition(new FdoProfileRule());
  }

  @Override
  public ImmutableList<RuleSet> requires() {
    return ImmutableList.of(CoreRules.INSTANCE, PlatformRules.INSTANCE);
  }
}
