# jludd

jludd aims to be a free implementation of the [OpenUDC protocol](https://github.com/Open-UDC/open-udc), which allows to create new P2P crypto-currencies based on individuals and Universal Dividend.

If you want to learn more on its concepts, go to [concepts page](https://github.com/c-geek/jludd/blob/master/concepts.md).

## Installation

jludd is a Java application with an embedded HTTP server (Jetty), so you need Java installed and Maven (Java compiler). Here is an example for Ubuntu installation:

    $ sudo apt-get install openjdk-6-jre maven

And then, just install jludd:

    git clone git@github.com:c-geek/jludd.git
    cd jludd
    ./install

Note that this is still a handmade installation, but auto-installation packages (such as .deb) will be proposed as soon as the application has its core features developed.

## Get jludd run

Just launch it using the following command:

    $ ./launch

By default, jludd runs on port 8080 and cannot be changed for the moment (this will evolve very soon).

## Disclaimer

jludd only *aims* to be an implementation of the OpenUDC protocol, but it is not. Firstly because OpenUDC protocol is still a in drafting state, and secondly because jludd have some divergences in its mecanisms.
Consequently, jludd proposes its own protocol which differs with OpenUDC. However, we hope that those protocols will join at some point.

## Get involved in jludd project

For the moment jludd is developed only by its author. If you wish to participate/debate on it, you may join OpenUDC XMPP chatroom (open-udc@muc.jappix.com) on [OpenUDC blog](http://www.openudc.org/) (chat is available on the bottom-right corner of the blog) and contact *cgeek*.

# References

* Official OpenUDC project website: <http://www.openudc.org>
* Official OpenUDC repository: <https://github.com/Open-UDC/open-udc>
* Other project trying to implement OpenUDC in python: <https://github.com/canercandan/django-openudc>
* Theoretical reference: [Relativity Theory of Money v2.718, Stephane Laborde - Nov. 2012](http://wiki.creationmonetaire.info/index.php?title=Main_Page)

# License

Copyright (c) 2013 The jludd team.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.