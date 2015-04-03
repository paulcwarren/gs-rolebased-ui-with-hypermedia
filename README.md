# gs-rolebased-ui-with-hypermedia

Most, if not all, modern enterprise software needs to have a role-based user interface to support common-place use cases like, for example, devolved administration; i.e. User X is an coordinator for Group Y, allowing him or her to perform admin functions for that Group.  This is just an example, of course, there are plenty of others.

Spring provides support for many common authentication and authorization patterns and standards like OAuth2 and user management through LDAP or CAS.  It also supports maturing REST models such as hypermedia through Spring HATEOAS and Spring Data REST.

On the client-side, enterprises usually like to consolidate on a UI technology so that their apps have a similar look and feel.  Obviously, angularjs is right up there (at the time of writing anyways…polymer anyone?) driven largely, I think, by its extensibility.  Seems now-a-days that pretty much any service or directive you can think of is out there in github as a bower component.  One such extension, pertinent to this article, being angular-hal. 

This is a little investigation into how one might implement a role-based UI in a modern day SPA by pairing these technologies together. 

[Read more...](https://paulcwarren.wordpress.com/2015/04/03/role-based-spas-with-angularjs-and-spring-hateoas/)

