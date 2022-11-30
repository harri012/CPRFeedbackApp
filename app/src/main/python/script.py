import numpy as np
from scipy.integrate import simpson

import numpy as np
from scipy.integrate import simpson, trapezoid

def displacementPost(accelerationList, compressionTime):

    velocityList = []
    displacementList = []

    for count, acceleration in enumerate(accelerationList):

        x = np.linspace(0, compressionTime[count], len(acceleration))

        velocityList.append(simpson(acceleration, x))

    for count, velocity in enumerate(velocityList):
        displacementList.append(velocity * time[count])

    return displacementList

accelerationList = [[0,0,0],[1,2,3],[9,8,7]]
time = [2.5, 6.9, 5.2]


def displacementLive(accelerationList, compressionTime):

    x = np.linspace(0, compressionTime, len(accelerationList))
    velocity = simpson(accelerationList, x)
    displacement = velocity * compressionTime

    return displacement