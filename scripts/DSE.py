#!/usr/bin/env python

import os
import sys
import multiprocessing as mp
from subprocess import call
from termcolor import colored, cprint 

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))
DEFAULT_BUILD_DIR = os.path.join(ROOT_DIR, '../MaxDeep/build')

class MaxDeepBuildParam(object):
    def __init__(self, F, N):
        self.freq = F
        self.pipe = N

    def getParams(self):
        return [
            'FREQ=%d' % self.freq,
            'NUM_PIPES=%d' % self.pipe
        ]

def build(params):
    print 'Running build ...'
    print 'ROOT_DIR:  %s' % ROOT_DIR
    print 'BUILD_DIR: %s' % DEFAULT_BUILD_DIR
    os.chdir(DEFAULT_BUILD_DIR)
    print 'CURR_DIR:  %s' % os.path.abspath('./')
    print 'Run make'
    call(['make', 'build'] + params.getParams())

if __name__ == '__main__':
    print '%s for MaxDeep' % colored('Design Space Exploration', 'cyan', attrs=['blink', 'reverse'])

    processes = [
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 1), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 2), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 4), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 8), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 12), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 16), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 20), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 24), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 28), )),
        mp.Process(target=build, args=(MaxDeepBuildParam(100, 32), )),
    ]

    idx = 0
    num_pipes = 2
    while idx < len(processes):
        ps = processes[idx:idx+num_pipes]
        for p in ps:
            p.start()
        for p in ps:
            p.join()
        idx += num_pipes

