import numpy as np
import scipy.integrate as integrate

def displacementPost(accelerationList, compressionTime):

    velocityList = []
    displacementList = []

    for count, acceleration in enumerate(accelerationList):

        x = np.linspace(0, compressionTime[count], len(acceleration))

        velocityList.append(scp.integrate.simpson(acceleration, x))

    for count, velocity in enumerate(velocityList):
        displacementList.append(velocity * time[count])

    return displacementList


def displacementLive(accelerationList, compressionTime):

    x = np.linspace(0, compressionTime, len(accelerationList))
    velocity = integrate.simps(accelerationList, x)
    displacement = velocity * compressionTime

    return abs(displacement)