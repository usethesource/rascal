package org.rascalmpl.uri.vfs;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;

import io.usethesource.vallang.ISourceLocation;

public interface IRemoteResolverRegistryClient {
    @JsonNotification("rascal/vfs/watcher/sourceLocationChanged")
    void sourceLocationChanged(ISourceLocation root, int type, String watchId);
}
