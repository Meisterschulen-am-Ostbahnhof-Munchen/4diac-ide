<?xml version="1.0" encoding="UTF-8"?>
<ResourceType Name="EMB_RES" Comment="Most basic resource for executing FB networks">
	<Identification Function="This resource provides the most basic functionality of an IEC 61499 resource, namely executing FB networks. For convenience it already contains an instance of the E_RESTART FB providing events for starting up and shutting down an application.

This resource is based on examples found in the different parts of IEC 61499 and the documentation at http://www.holobloc.com/doc/fb/rt/EMB_RES.htm" Description="Copyright (c) 2017 fortiss GmbH&#10; &#10;This program and the accompanying materials are made&#10;available under the terms of the Eclipse Public License 2.0&#10;which is available at https://www.eclipse.org/legal/epl-2.0/&#10;&#10;SPDX-License-Identifier: EPL-2.0">
	</Identification>
	<VersionInfo Version="3.0" Author="Patrick Aigner" Date="2025-04-14" Remarks="changed package">
	</VersionInfo>
	<VersionInfo Organization="fortiss GmbH" Version="1.0" Author="Alois Zoitl" Date="2017-12-02">
	</VersionInfo>
	<CompilerInfo packageName="iec61499::system">
	</CompilerInfo>
	<FBNetwork>
		<FB Name="START" Type="iec61499::events::E_RESTART" x="100" y="0">
		</FB>
	</FBNetwork>
</ResourceType>
