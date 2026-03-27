package org.rascalmpl.uri.vfs;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationChanged;

public interface IRemoteResolverRegistryClient {
    @JsonNotification("rascal/vfs/watcher/sourceLocationChanged")
    void sourceLocationChanged(ISourceLocationChanged changed);
}
