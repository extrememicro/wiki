/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.wiki.tree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.exoplatform.wiki.mow.api.Wiki;
import org.exoplatform.wiki.mow.api.WikiType;
import org.exoplatform.wiki.service.WikiPageParams;
import org.exoplatform.wiki.tree.utils.TreeUtils;
import org.exoplatform.wiki.utils.Utils;

public class SpaceTreeNode extends TreeNode {
  
  public SpaceTreeNode(String name) throws Exception {
    super(name, TreeNodeType.SPACE);
    this.path = buildPath();

    try {
      WikiType wikiType = WikiType.valueOf(name.toUpperCase());
      this.hasChild = Utils.getWikisByType(wikiType).size() > 0;
    } catch (IllegalArgumentException ex) {
      this.hasChild = false;
    }
  }  
  
  @Override
  protected void addChildren(HashMap<String, Object> context) throws Exception {
    try {
      WikiType wikiType = WikiType.valueOf(name.toUpperCase());
      Collection<Wiki> wikis = Utils.getWikisByType(wikiType);
      Iterator<Wiki> childWikiIterator = wikis.iterator();
      int count = 0;
      int size = getNumberOfChildren(context, wikis.size());
      while (childWikiIterator.hasNext() && count < size) {
        WikiTreeNode child = new WikiTreeNode(childWikiIterator.next());
        this.children.add(child);
        count++;
      }
      super.addChildren(context);
    } catch (IllegalArgumentException ex) {
      this.hasChild = false;
    }
  }

  public WikiTreeNode getChildByName(String name) throws Exception {
    for (TreeNode child : children) {
      if (child.getName().equals(name))
        return (WikiTreeNode)child;
    }
    return null;
  }
  
  @Override
  public String buildPath() {
    return TreeUtils.getPathFromPageParams(new WikiPageParams(this.name, null, null));
  }
}
