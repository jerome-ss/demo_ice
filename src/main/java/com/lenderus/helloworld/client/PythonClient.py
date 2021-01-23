#!/usr/bin/env python
# **********************************************************************
#
# Copyright (c) 2003-2013 ZeroC, Inc. All rights reserved.
#
# This copy of Ice is licensed to you under the terms described in the
# ICE_LICENSE file included in this distribution.
#
# **********************************************************************

import sys, traceback, Ice

Ice.loadSlice("D:\\Java\\workspace\\workspace-temp\\test_ice\\slice\\myservice.ice")
import demo

status = 0
ice = None
try:
    ic = Ice.initialize(sys.argv)
    base = ic.stringToProxy("MyService:default -p 10001")
    printer = demo.MyServicePrx.checkedCast(base)
    if not printer:
        raise RuntimeError("Invalid proxy")

    result = printer.hello()
    print result
except:
    traceback.print_exc()
    status = 1

if ic:
    # Clean up
    try:
        ic.destroy()
    except:
        traceback.print_exc()
        status = 1

sys.exit(status)
