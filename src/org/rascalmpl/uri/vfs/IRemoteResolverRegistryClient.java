package org.rascalmpl.uri.vfs;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment;
import org.rascalmpl.uri.remote.jsonrpc.ISourceLocationChanged;

@JsonSegment("rascal/vfs/watcher")
public interface IRemoteResolverRegistryClient {
    @JsonNotification
    void sourceLocationChanged(ISourceLocationChanged changed);
}
