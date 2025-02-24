/*
 * Copyright (c) 2018-2025, NWO-I CWI, Swat.engineering and Paul Klint
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.rascalmpl.runtime;

import java.util.List;
import java.util.stream.Collectors;

import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.parser.gtd.result.action.IActionExecutor;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.IValue;

/**
 * This is the way of executing filters for Rascal syntax definitions. 
 * A set of "filter" functions is provided which are called at every
 * level in the tree with as parameter the tree to be constructed.
 * If the filter functions do nothing, nothing happens. If they 
 * return a new tree, the old tree is substituted by the new tree.
 * If they throw the `filter` exception, then the branch up to
 * the first surrounding ambiguity cluster is filtered.
 */
public class RascalFunctionActionExecutor implements IActionExecutor<ITree> {
	private final boolean isPure;
	private List<IFunction> filters;

	public RascalFunctionActionExecutor(ISet functions, boolean isPure) {
		this.isPure = isPure;
        assert !functions.isEmpty();
		this.filters = functions.stream().map(v -> (IFunction) v).collect(Collectors.toList());
	}
	
	public void completed(Object environment, boolean filtered) {

	}

	public Object createRootEnvironment() {
		return new Object();
	}

	public Object enteringListNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringListProduction(Object production, Object env) {
		return env;
	}

	public Object enteringNode(Object production, int index, Object environment) {
		return environment;
	}

	public Object enteringProduction(Object production, Object env) {
		return env;
	}

	public void exitedListProduction(Object production, boolean filtered, Object environment) {

	}

	public void exitedProduction(Object production, boolean filtered, Object environment) {
	}

	public ITree filterAmbiguity(ITree ambCluster, Object environment) {
	    if (!TreeAdapter.isAmb(ambCluster)) {
	        // this may happen when due to filtering during the bottom-up
	        // construction of a tree a singleton amb cluster was build
	        // and then canonicalized to the single instance tree.
	        // nothing to worry about. 
	        return ambCluster;
	    }
		
		if (TreeAdapter.getAlternatives(ambCluster).size() == 0) {
			return null;
		}

		if (filters == null) {
			return ambCluster;
		}

        for (IFunction f : filters) {
            try {
                ITree result = f.call(ambCluster);

                if (!TreeAdapter.isAmb(result)) {
                    return result;
                }
                else if (TreeAdapter.getAlternatives(result).size() == 1) {
                    return (ITree) TreeAdapter.getAlternatives(result).iterator().next();
                }
                else {
                    return result;
                }
            } 
            catch (MatchFailed m) {
                return ambCluster;
            }
            catch (Throw t) {
                IValue exc = t.getException();

				if (exc.getType().isAbstractData() && ((IConstructor) exc).getConstructorType() == RuntimeExceptionFactory.CallFailed) {
					return ambCluster;
				}
				else {
					throw t;
				}
            }
            catch (Filtered x) {
                return null;
            }
        }

        return ambCluster;
	}

	@Override
	public ITree filterCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListAmbiguity(ITree ambCluster, Object environment) {
		return filterAmbiguity(ambCluster, environment);
	}

	@Override
	public ITree filterListCycle(ITree cycle, Object environment) {
		return cycle;
	}

	@Override
	public ITree filterListProduction(ITree tree, Object environment) {
		return tree;
	}

	@Override
	public ITree filterProduction(ITree tree, Object environment) {
		if (filters == null) {
			return tree;
		}

        for (IFunction f : filters) {
            try {
                ITree result = (ITree) f.call(tree);

                if (!TreeAdapter.isAmb(result)) {
                    return result;
                }
                else if (TreeAdapter.getAlternatives(result).size() == 1) {
                    return (ITree) TreeAdapter.getAlternatives(result).iterator().next();
                }
                else {
                    return result;
                }
            }
            catch (MatchFailed m) {	
                return tree;
            }
            catch (Throw t) {
                IValue exc = t.getException();

                if (exc.getType().isAbstractData() && ((IConstructor) exc).getConstructorType() == RuntimeExceptionFactory.CallFailed) {
                    return tree;
                }
                else {
                    throw t;
                }
            }
            catch(Filtered x) {
                return null;
            }
        }

        return tree;
	}

	public boolean isImpure(Object rhs) {
		return !isPure;
	}
}
