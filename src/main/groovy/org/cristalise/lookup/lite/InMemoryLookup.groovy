package org.cristalise.lookup.lite;

import groovy.transform.CompileStatic

import org.cristalise.kernel.common.ObjectNotFoundException
import org.cristalise.kernel.lookup.AgentPath
import org.cristalise.kernel.lookup.DomainPath
import org.cristalise.kernel.lookup.InvalidItemPathException
import org.cristalise.kernel.lookup.ItemPath
import org.cristalise.kernel.lookup.Lookup
import org.cristalise.kernel.lookup.Path
import org.cristalise.kernel.lookup.RolePath
import org.cristalise.kernel.process.auth.Authenticator
import org.cristalise.kernel.property.Property
import org.cristalise.kernel.property.PropertyDescriptionList
import org.cristalise.kernel.utils.Logger


@CompileStatic
abstract class InMemoryLookup implements Lookup {

    protected Map cache = [:] //LinkedHashMap

    private Iterator<Path> getEmptyPathIter() {
        return new Iterator<Path>() {
            public boolean hasNext() { return false; }
            public Path next() { return null; }
            public void remove() {}
        };
    }
    
    /**
     * 
     * @param key
     * @return
     */
    private Path retrievePath(String key) throws ObjectNotFoundException {
        Logger.msg(5, "InMemoryLookup.retrievePath() - key: $key");

        Path p = (Path) cache[key]
        
        if(p) return p
        else throw new ObjectNotFoundException("$key does not exist")
    }

    /**
     * Connect to the directory using the credentials supplied in the Authenticator. 
     * 
     * @param user The connected Authenticator. The Lookup implementation may use the AuthObject in this to communicate with the database.
     */
    @Override
    public void open(Authenticator user) {
        Logger.msg(8, "InMemoryLookup.open() - Do nothing");
    }

    /**
     * Shutdown the lookup
     */
    @Override
    public void close() {
        Logger.msg(8, "InMemoryLookup.close() - Do nothing");
    }

    /**
     * Fetch the correct subclass class of ItemPath for a particular Item, derived from its lookup entry. 
     * This is used by the CORBA Server to make sure the correct Item subclass is used. 
     * 
     * @param sysKey The system key of the Item
     * @return an ItemPath or AgentPath
     * @throws InvalidItemPathException When the system key is invalid/out-of-range
     * @throws ObjectNotFoundException When the Item does not exist in the directory.
     */
    @Override
    public ItemPath getItemPath(String sysKey) throws InvalidItemPathException, ObjectNotFoundException {
        return (ItemPath) retrievePath(new ItemPath(sysKey).string);
    }

    @Override
    public AgentPath getAgentPath(String agentName) throws ObjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePath getRolePath(String roleName) throws ObjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Find the ItemPath for which a DomainPath is an alias.
     * 
     * @param domainPath The path to resolve
     * @return The ItemPath it points to (should be an AgentPath if the path references an Agent)
     * @throws InvalidItemPathException
     * @throws ObjectNotFoundException
     */
    @Override
    public ItemPath resolvePath(DomainPath domainPath) throws InvalidItemPathException, ObjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Resolve a path to a CORBA Object Item or Agent
     * 
     * @param path The path to be resolved
     * @return The CORBA Object
     * @throws ObjectNotFoundException When the Path doesn't exist, or doesn't have an IOR associated with it
     */
    @Override
    public String getIOR(Path path) throws ObjectNotFoundException {
        Logger.msg(5, "InMemoryLookup.getIOR() - Path: $path.string");

        return retrievePath(path.string).IOR.toString();
    }

    /**
     * Checks if a particular Path exists in the directory
     * 
     * @param path The path to check
     * @return boolean true if the path exists, false if it doesn't
     */
    @Override
    public boolean exists(Path path) {
        Logger.msg(5, "InMemoryLookup.exists() - Path: $path.string");
        
        
        
        return cache.containsKey(path.string);
    }

    @Override
    public Iterator<Path> getChildren(Path path) {
        Logger.msg(5, "InMemoryLookup.getChildren() - Path: $path.string");

        // TODO: Implement search
        
        return getEmptyPathIter();
    }

    @Override
    public Iterator<Path> search(Path start, String name) {
        Logger.msg(5, "InMemoryLookup.search() - Path: $start, Name: $name");

        // TODO: Implement search
        
        return getEmptyPathIter();
    }

    @Override
    public Iterator<Path> search(Path start, Property... props) {
        Logger.msg(5, "InMemoryLookup.search() - Path: $start, # of props: $props.length, props[0]: ${props[0].name} - ${props[0].value}");

        // TODO: Implement search

        return getEmptyPathIter();
    }

    @Override
    public Iterator<Path> search(Path start, PropertyDescriptionList props) {
        Logger.msg(5, "InMemoryLookup.search() - Path: $start, # of props: $props.list.size");
        
        // TODO: Implement search

        return getEmptyPathIter();
    }

    @Override
    public Iterator<Path> searchAliases(ItemPath itemPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AgentPath[] getAgents(RolePath rolePath) throws ObjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RolePath[] getRoles(AgentPath agentPath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasRole(AgentPath agentPath, RolePath role) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getAgentName(AgentPath agentPath) throws ObjectNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }
}
