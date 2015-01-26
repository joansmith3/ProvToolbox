package org.openprovenance.prov.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;


/** This class provides a set of indexes over information contained in
 * an Document, facilitating its navigation.  Its constructor takes an
 * Document builds an index for it.  Of course, for the index to be
 * maintained, one cannot access, say the list of edges, and mutate
 * it. Instead, one has to use the add methods provided.
 *<p>
 * Note that code is not thread-safe.

 TODO: index annotation, index edges

 */

public class IndexedDocument implements StatementAction {



    ProvUtilities u=new ProvUtilities();
    final ProvFactory pFactory;


    private HashMap<QualifiedName,Entity>   entityMap=new HashMap<QualifiedName, Entity>();
    private HashMap<QualifiedName,Activity> activityMap=new HashMap<QualifiedName, Activity>();
    private HashMap<QualifiedName,Agent>    agentMap=new HashMap<QualifiedName, Agent>();

    /* Collection of Used edges that have a given process as an
     * effect. */
    private HashMap<QualifiedName,Collection<Used>> activityUsedMap=new HashMap<QualifiedName, Collection<Used>>();

    /* Collection of Used edges that have a given entity as a
     * cause. */
    private HashMap<QualifiedName,Collection<Used>> entityUsedMap=new HashMap<QualifiedName, Collection<Used>>();
    private Collection<Used> allUsed=new LinkedList<Used>();

    /* Collection of WasGeneratedBy edges that have a given activity as a
     * cause. */
    private HashMap<QualifiedName,Collection<WasGeneratedBy>> activityWasGeneratedByMap=new HashMap<QualifiedName, Collection<WasGeneratedBy>>();

    /* Collection of WasGeneratedBy edges that have a given entity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasGeneratedBy>> entityWasGeneratedByMap=new HashMap<QualifiedName, Collection<WasGeneratedBy>>();
    private Collection<WasGeneratedBy> allWasGeneratedBy=new LinkedList<WasGeneratedBy>();

    /* Collection of WasDerivedFrom edges that have a given entity as a cause. */
    private HashMap<QualifiedName,Collection<WasDerivedFrom>> entityCauseWasDerivedFromMap=new HashMap<QualifiedName, Collection<WasDerivedFrom>>();

    /* Collection of WasDerivedFrom edges that have a given entity as an
     * effect. */
    private HashMap<String,Collection<WasDerivedFrom>> entityEffectWasDerivedFromMap=new HashMap<String, Collection<WasDerivedFrom>>();
    private Collection<WasDerivedFrom> allWasDerivedFrom=new LinkedList<WasDerivedFrom>();

    /* Collection of WasAssociatedWith edges that have a given activity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasAssociatedWith>> activityWasAssociatedWithMap=new HashMap<QualifiedName, Collection<WasAssociatedWith>>();

    /* Collection of WasAssociatedWith edges that have a given agent as a
     * cause. */
    private HashMap<QualifiedName,Collection<WasAssociatedWith>> agentWasAssociatedWithMap=new HashMap<QualifiedName, Collection<WasAssociatedWith>>();
    private Collection<WasAssociatedWith> allWasAssociatedWith=new LinkedList<WasAssociatedWith>();

    /* Collection of WasInformedBy edges that have a given activity as a cause. */
    private HashMap<QualifiedName,Collection<WasInformedBy>> activityCauseWasInformedByMap=new HashMap<QualifiedName, Collection<WasInformedBy>>();

    /* Collection of WasInformedBy edges that have a given activity as an
     * effect. */
    private HashMap<QualifiedName,Collection<WasInformedBy>> activityEffectWasInformedByMap=new HashMap<QualifiedName, Collection<WasInformedBy>>();
    private Collection<WasInformedBy> allWasInformedBy=new LinkedList<WasInformedBy>();
    private Namespace nss;


    /** Return all used edges for this graph. */
    public Collection<Used> getUsed() {
        return allUsed;
    }
    /** Return all used edges with activity p as an effect. */
    public Collection<Used> getUsed(Activity p) {
        return activityUsedMap.get(p.getId());
    }

    /** Return all used edges with entity a as a cause. */
    public Collection<Used> getUsed(Entity p) {
        return entityUsedMap.get(p.getId());
    }

    /** Return all WasGeneratedBy edges for this graph. */
    public Collection<WasGeneratedBy> getWasGeneratedBy() {
        return allWasGeneratedBy;
    }
    /** Return all WasGeneratedBy edges with activity p as an effect. */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Activity p) {
        return activityWasGeneratedByMap.get(p.getId());
    }

    /** Return all WasGeneratedBy edges with entity a as a cause. */
    public Collection<WasGeneratedBy> getWasGeneratedBy(Entity p) {
        return entityWasGeneratedByMap.get(p.getId());
    }

    /** Return all WasDerivedFrom edges for this graph. */
    public Collection<WasDerivedFrom> getWasDerivedFrom() {
        return allWasDerivedFrom;
    }
    /** Return all WasDerivedFrom edges with entity a as a cause. */
    public Collection<WasDerivedFrom> getWasDerivedFromWithCause(Entity a) {
        return entityCauseWasDerivedFromMap.get(a.getId());
    }

    /** Return all WasDerivedFrom edges with entity a as an effect . */
    public Collection<WasDerivedFrom> getWasDerivedFromWithEffect(Entity a) {
        return entityEffectWasDerivedFromMap.get(a.getId());
    }


    /** Return all WasInformedBy edges for this graph. */
    public Collection<WasInformedBy> getWasInformedBy() {
        return allWasInformedBy;
    }
    /** Return all WasInformedBy edges with activity p as a cause. */
    public Collection<WasInformedBy> getWasInformedByWithCause(Activity a) {
        return activityCauseWasInformedByMap.get(a.getId());
    }

    /** Return all WasInformedBy edges with activity a as an effect. */
    public Collection<WasInformedBy> getWasInformedByWithEffect(Activity a) {
        return activityEffectWasInformedByMap.get(a.getId());
    }

    /** Return all WasAssociatedWith edges for this graph. */
    public Collection<WasAssociatedWith> getWasAssociatedWith() {
        return allWasAssociatedWith;
    }
    /** Return all WasAssociatedWith edges with activity p as an effect. */
    public Collection<WasAssociatedWith> getWasAssociatedWith(Activity p) {
        return activityWasAssociatedWithMap.get(p.getId());
    }

    /** Return all WasAssociatedWith edges with entity a as a cause. */
    public Collection<WasAssociatedWith> getWasAssociatedWith(Agent a) {
        return agentWasAssociatedWithMap.get(a.getId());
    
}




    public Entity add(Entity entity) {
        return add(entity.getId(),entity);
    }
    public Entity add(QualifiedName name, Entity entity) {
        Entity existing=entityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,entity);
            return existing;
        } else {
            entityMap.put(name,entity);
            return entity;
        }
    }



    void mergeAttributes(Element existing, Element newElement) {
	Set<LangString> set=new HashSet<LangString>(newElement.getLabel());
	set.removeAll(existing.getLabel());
	existing.getLabel().addAll(set);
	
	Set<Location> set2=new HashSet<Location>(newElement.getLocation());
	set2.removeAll(existing.getLocation());
	existing.getLocation().addAll(set2);
	
	Set<Type> set3=new HashSet<Type>(newElement.getType());
	set3.removeAll(existing.getType());
	existing.getType().addAll(set3);
	
	Set<Other> set4=new HashSet<Other>(newElement.getOther());
	set4.removeAll(existing.getOther());
	existing.getOther().addAll(set4);	
    }
    
    public Agent add(Agent agent) {
        return add(agent.getId(),agent);
    }
    public Agent add(QualifiedName name, Agent agent) {
        Agent existing=agentMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,agent);
            return existing;
        } else {
            agentMap.put(name,agent);
            return agent;
        }
    }

    public Activity add(Activity activity) {
        return add(activity.getId(),activity);
    }

    public Activity add(QualifiedName name, Activity activity) {
        Activity existing=activityMap.get(name);
        if (existing!=null) {
            mergeAttributes(existing,activity);
            return existing;
        } else {
            activityMap.put(name,activity);
            return activity;
        }
    }

    public Activity getActivity(String name) {
        return activityMap.get(name);
    }
    public Entity getEntity(String name) {
        return entityMap.get(name);
    }
    public Agent getAgent(String name) {
        return agentMap.get(name);
    }

            
    public IndexedDocument(ProvFactory pFactory, Document doc) {
        this.pFactory=pFactory;
        this.nss=doc.getNamespace();
        
        u.forAllStatementOrBundle(doc.getStatementOrBundle(), this);

        /*

        if (graph.getDependencies()!=null) {
            if (getDependencies()==null) {
                setDependencies(of.createDependencies());
            }
            List<Edge> edges=u.getEdges(graph);

            for (Edge edge: edges) {
                if (edge instanceof Used) {
                    addUsed(pFactory.newUsed((Used) edge));  
                }
                if (edge instanceof WasGeneratedBy) {
                    addWasGeneratedBy(pFactory.newWasGeneratedBy((WasGeneratedBy) edge));  
                }
                if (edge instanceof WasDerivedFrom) {
                    addWasDerivedFrom(pFactory.newWasDerivedFrom((WasDerivedFrom) edge));  
                }
                if (edge instanceof WasAssociatedWith) {
                    addWasAssociatedWith(pFactory.newWasAssociatedWith((WasAssociatedWith) edge));  
                }
                if (edge instanceof WasInformedBy) {
                    addWasInformedBy(pFactory.newWasInformedBy((WasInformedBy) edge));  
                }
            }
        }
        */
    }



    /** Add a used edge to the graph. Update activityUsedMap and
        entityUsedMap accordingly.  Used edges with different attributes are considered distinct.
        */

    public Used add(Used used) {
        QualifiedName aid=used.getActivity();
        QualifiedName eid=used.getEntity();

        used=pFactory.newUsed(used); //clone

        boolean found=false;
        Collection<Used> ucoll=activityUsedMap.get(aid);
        if (ucoll==null) {
            ucoll=new LinkedList<Used>();
            ucoll.add(used);
            activityUsedMap.put(aid,ucoll);
        } else {
            for (Used u: ucoll) {               
                if (u.equals(used)) {               
                    found=true;
                    used=u;
                    break;
                }
            }
            if (!found) {
                ucoll.add(used);
            }
        }

        ucoll=entityUsedMap.get(eid);
        if (ucoll==null) {
            ucoll=new LinkedList<Used>();
            ucoll.add(used);
            entityUsedMap.put(eid,ucoll);
        } else {
            if (!found) {
                // if we had not found it in the first table, then we
                // have to add it here too
                ucoll.add(used);
            }
        }

        if (!found) {
            allUsed.add(used);
        }
        return used;
   }

    public WasGeneratedBy add(WasGeneratedBy wgb) {
	QualifiedName aid = wgb.getActivity();
	QualifiedName eid = wgb.getEntity();

	wgb = pFactory.newWasGeneratedBy(wgb);

	boolean found = false;
	Collection<WasGeneratedBy> gcoll = activityWasGeneratedByMap.get(aid);
	if (gcoll == null) {
	    gcoll = new LinkedList<WasGeneratedBy>();
	    gcoll.add(wgb);
	    activityWasGeneratedByMap.put(aid, gcoll);
	} else {

	    for (WasGeneratedBy u : gcoll) {

		if (u.equals(wgb)) {
		    found = true;
		    wgb=u; 
		    break;
		}
	    }
	    if (!found) {
		gcoll.add(wgb);
	    }
	}

	gcoll = entityWasGeneratedByMap.get(eid);
	if (gcoll == null) {
	    gcoll = new LinkedList<WasGeneratedBy>();
	    gcoll.add(wgb);
	    entityWasGeneratedByMap.put(eid, gcoll);
	} else {
	    if (!found) {
		// if we had not found it in the first table, then we
		// have to add it here too
		gcoll.add(wgb);
	    }
	}

	if (!found) {
	    allWasGeneratedBy.add(wgb);
	}
	return wgb;
    }



    @Override
    public void doAction(Activity s) {
	add(s);
	
    }
    @Override
    public void doAction(Used s) {
	add(s);
    }
    
    @Override
    public void doAction(WasStartedBy s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Agent s) {
	add(s);	
    }
    @Override
    public void doAction(AlternateOf s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasAssociatedWith s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasAttributedTo s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasInfluencedBy s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(ActedOnBehalfOf s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(WasDerivedFrom s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(DictionaryMembership s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(DerivedByRemovalFrom s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(WasEndedBy s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Entity s) {
	add(s);	
    }
    @Override
    public void doAction(WasGeneratedBy s) {
	add(s);
    }
    @Override
    public void doAction(WasInvalidatedBy s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(HadMember s) {
	throw new UnsupportedOperationException();		
    }
    @Override
    public void doAction(MentionOf s) {
	throw new UnsupportedOperationException();			
    }
    @Override
    public void doAction(SpecializationOf s) {
	throw new UnsupportedOperationException();			
	
    }
    @Override
    public void doAction(DerivedByInsertionFrom s) {
	throw new UnsupportedOperationException();
    }
    @Override
    public void doAction(WasInformedBy s) {
	throw new UnsupportedOperationException();	
    }
    @Override
    public void doAction(Bundle s, ProvUtilities provUtilities) {
	throw new UnsupportedOperationException();	
    }
    
    public Document toDocument() {
	Document res=pFactory.newDocument();
	res.getStatementOrBundle().addAll(entityMap.values());
	res.getStatementOrBundle().addAll(activityMap.values());
	res.getStatementOrBundle().addAll(agentMap.values());
	res.getStatementOrBundle().addAll(allUsed);
	res.getStatementOrBundle().addAll(allWasGeneratedBy);
	res.getStatementOrBundle().addAll(allWasDerivedFrom);
	res.setNamespace(nss);
	return res;
    }

}


   //  /** Add a wasGeneratedBy edge to the graph. Update activityWasGeneratedByMap and
   //      entityWasGeneratedByMap accordingly.  By doing so, aggregate all wasGeneratedBy
   //      edges (a,r,p) with different accounts in a single edge.
   //      Return the wasGeneratedBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasGeneratedBy addWasGeneratedBy(WasGeneratedBy wasGeneratedBy) {
   //      ActivityRef pid=wasGeneratedBy.getCause();
   //      Activity p=(Activity)(pid.getRef());
   //      EntityRef aid=wasGeneratedBy.getEffect();
   //      Entity a=(Entity)(aid.getRef());
   //      Role r=wasGeneratedBy.getRole();
   //      Collection<AccountRef> accs=wasGeneratedBy.getAccount();

   //      WasGeneratedBy result=wasGeneratedBy;

   //      boolean found=false;
   //      Collection<WasGeneratedBy> gcoll=activityWasGeneratedByMap.get(p.getId());
   //      if (gcoll==null) {
   //          gcoll=new LinkedList();
   //          gcoll.add(wasGeneratedBy);
   //          activityWasGeneratedByMap.put(p.getId(),gcoll);
   //      } else {

   //          for (WasGeneratedBy u: gcoll) {
                
   //              if (aid.equals(u.getEffect())
   //                  &&
   //                  r.equals(u.getRole())) {
   //                  addNewAccounts(u.getAccount(),accs);
   //                  result=u;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              gcoll.add(wasGeneratedBy);
   //          }
   //      }

   //      gcoll=entityWasGeneratedByMap.get(a.getId());
   //      if (gcoll==null) {
   //          gcoll=new LinkedList();
   //          gcoll.add(wasGeneratedBy);
   //          entityWasGeneratedByMap.put(a.getId(),gcoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              gcoll.add(wasGeneratedBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasGeneratedBy.add(wasGeneratedBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasGeneratedBy);
   //      }
   //      return result;
   // }


   //  /** Add a wasDerivedFrom edge to the graph. Update activityWasDerivedFromMap and
   //      entityWasDerivedFromMap accordingly.  By doing so, aggregate all wasDerivedFrom
   //      edges (a1,r,a2) with different accounts in a single edge.
   //      Return the wasDerivedFrom edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasDerivedFrom addWasDerivedFrom(WasDerivedFrom wasDerivedFrom) {
   //      EntityRef aid2=wasDerivedFrom.getEffect();
   //      Entity a2=(Entity)(aid2.getRef());
   //      EntityRef aid1=wasDerivedFrom.getCause();
   //      Entity a1=(Entity)(aid1.getRef());
   //      Collection<AccountRef> accs=wasDerivedFrom.getAccount();

   //      WasDerivedFrom result=wasDerivedFrom;

   //      boolean found=false;
   //      Collection<WasDerivedFrom> dcoll=entityCauseWasDerivedFromMap.get(a1.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasDerivedFrom);
   //          entityCauseWasDerivedFromMap.put(a1.getId(),dcoll);
   //      } else {

   //          for (WasDerivedFrom d: dcoll) {
                
   //              if ((aid1.equals(d.getCause())) && (aid2.equals(d.getEffect()))) {
   //                  addNewAccounts(d.getAccount(),accs);
   //                  result=d;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              dcoll.add(wasDerivedFrom);
   //          }
   //      }

   //      dcoll=entityEffectWasDerivedFromMap.get(a2.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasDerivedFrom);
   //          entityEffectWasDerivedFromMap.put(a2.getId(),dcoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              dcoll.add(wasDerivedFrom);
   //          }
   //      }

   //      if (!found) {
   //          allWasDerivedFrom.add(wasDerivedFrom);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasDerivedFrom);
   //      }

   //      return result;
   // }


   //  /** Add a wasControlledBy edge to the graph. Update activityWasAssociatedWithMap and
   //      agentWasAssociatedWithMap accordingly.  By doing so, aggregate all wasControlledBy
   //      edges (p,r,a) with different accounts in a single edge.
   //      Return the wasControlledBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasAssociatedWith addWasAssociatedWith(WasAssociatedWith wasControlledBy) {
   //      ActivityRef pid=wasControlledBy.getEffect();
   //      Activity p=(Activity)(pid.getRef());
   //      AgentRef aid=wasControlledBy.getCause();
   //      Agent a=(Agent)(aid.getRef());
   //      Role r=wasControlledBy.getRole();
   //      Collection<AccountRef> accs=wasControlledBy.getAccount();

   //      WasAssociatedWith result=wasControlledBy;

   //      boolean found=false;
   //      Collection<WasAssociatedWith> ccoll=activityWasAssociatedWithMap.get(p.getId());
   //      if (ccoll==null) {
   //          ccoll=new LinkedList();
   //          ccoll.add(wasControlledBy);
   //          activityWasAssociatedWithMap.put(p.getId(),ccoll);
   //      } else {

   //          for (WasAssociatedWith u: ccoll) {
                
   //              if (aid.equals(u.getCause())
   //                  &&
   //                  r.equals(u.getRole())) {
   //                  addNewAccounts(u.getAccount(),accs);
   //                  result=u;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              ccoll.add(wasControlledBy);
   //          }
   //      }

   //      ccoll=agentWasAssociatedWithMap.get(a.getId());
   //      if (ccoll==null) {
   //          ccoll=new LinkedList();
   //          ccoll.add(wasControlledBy);
   //          agentWasAssociatedWithMap.put(p.getId(),ccoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              ccoll.add(wasControlledBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasAssociatedWith.add(wasControlledBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasControlledBy);
   //      }
   //      return result;
   // }

   //  /** Add a wasTriggeredBy edge to the graph. Update activityWasInformedByMap and
   //      entityWasInformedByMap accordingly.  By doing so, aggregate all wasTriggeredBy
   //      edges (p1,r,p2) with different accounts in a single edge.
   //      Return the wasTriggeredBy edge itself (if it had not been encountered
   //      before), or the instance encountered before.*/

   //  public WasInformedBy addWasInformedBy(WasInformedBy wasTriggeredBy) {
   //      ActivityRef pid2=wasTriggeredBy.getEffect();
   //      Activity p2=(Activity)(pid2.getRef());
   //      ActivityRef pid1=wasTriggeredBy.getCause();
   //      Activity p1=(Activity)(pid1.getRef());
   //      Collection<AccountRef> accs=wasTriggeredBy.getAccount();

   //      WasInformedBy result=wasTriggeredBy;

   //      boolean found=false;
   //      Collection<WasInformedBy> dcoll=activityCauseWasInformedByMap.get(p1.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasTriggeredBy);
   //          activityCauseWasInformedByMap.put(p1.getId(),dcoll);
   //      } else {

   //          for (WasInformedBy d: dcoll) {
                
   //              if ( (pid1.equals(d.getCause())) && (pid2.equals(d.getEffect()))) {
   //                  addNewAccounts(d.getAccount(),accs);
   //                  result=d;
   //                  found=true;
   //              }
   //          }
   //          if (!found) {
   //              dcoll.add(wasTriggeredBy);
   //          }
   //      }

   //      dcoll=activityEffectWasInformedByMap.get(p2.getId());
   //      if (dcoll==null) {
   //          dcoll=new LinkedList();
   //          dcoll.add(wasTriggeredBy);
   //          activityEffectWasInformedByMap.put(p2.getId(),dcoll);
   //      } else {
   //          if (!found) {
   //              // if we had not found it in the first table, then we
   //              // have to add it here too
   //              dcoll.add(wasTriggeredBy);
   //          }
   //      }

   //      if (!found) {
   //          allWasInformedBy.add(wasTriggeredBy);
   //          getDependencies().getUsedOrWasGeneratedByOrWasInformedBy().add(wasTriggeredBy);
   //      }
   //      return result;
   // }